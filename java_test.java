package anemos.abs.partner.batch.application.dao.rfprediction.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import anemos.abs.partner.batch.application.dao.rfprediction.RfPredictionServiceDao;
import anemos.abs.ydncm.entity.adgrouptarget.PlacementCategory;
import anemos.abs.ydncm.entity.rfprediction.RfPrediction;
import anemos.abs.ydncm.entity.rfprediction.RfPredictionSelector;
import anemos.abs.ydncm.entity.rfprediction.RfPredictionServiceResponse;
import anemos.abs.ydncm.entity.rfprediction.RfPredictionOperationResponse;
import anemos.abs.ydncm.entity.rfprediction.RfPredictionTarget;
import anemos.abs.ydncm.enums.TargetType;

@Component
public class RfPredictionServiceDaoImpl implements RfPredictionServiceDao {

  private static final String RF_PREDICTION_PATH_TEMPLATE = "http://%s/ydncm/V1/RfPredictionService/%s";

  @Autowired
  RestTemplate restTemplate;

  @Value("${batch.rfcprs.campaign.advjYdncmHost}")
  private String host;

  @Override
  @Retryable(value = {Throwable.class}, maxAttempts = 2, backoff = @Backoff(delay = 1000))
  public String getPlcmtCtgryListName(Long accountId, Long rfPredictionId) {

    final String rfPredictionGetPath = String.format(RF_PREDICTION_PATH_TEMPLATE, host, "get");

    RfPredictionSelector selector = new RfPredictionSelector();
    selector.set_byUser("rfcprs_batch");
    selector.set_rid(String.valueOf(System.currentTimeMillis()));
    selector.set_urlBase("rfcprs_batch");
    selector.set_src("INTERNAL_TOOL");
    selector.setAccountId(accountId);
    selector.setRfPredictionIds(Collections.singletonList(rfPredictionId));
    RfPredictionServiceResponse res;

    try {
      res = restTemplate.postForObject(rfPredictionGetPath, selector, RfPredictionServiceResponse.class);
    } catch (Exception e) {
      throw new RuntimeException("RfPredictionService/get call error.", e);
    }
    if (res == null || res.getResultSet() == null || res.getResultSet().getResult() == null) {
      throw new RuntimeException("Api returned null result.");
    }
    if (res.getResultSet().getStatus() != HttpStatus.OK.value()) {
      throw new RuntimeException("Api returned unexpected status: " + res);
    }
    if (res.getResultSet().getError() != null) {
      throw new RuntimeException("Api returned error: " + res);
    }

    List<RfPrediction> rfPredictions = res.getResultSet().getResult().stream()
        .map(RfPredictionOperationResponse::getRfPrediction)
        .collect(Collectors.toList());

    for (RfPrediction rfPrediction : rfPredictions) {
      // リクエストで指定した rfPredictionId 以外のデータが返ってくることはない
      if (!rfPredictionId.equals(rfPrediction.getRfPredictionId())) {
        throw new RuntimeException(String.format("Unexpected rfPredictionId has been returned. original: %d, returned: %d", rfPredictionId, rfPrediction.getRfPredictionId()));
      }

      // Targets が存在しない場合はスキップ
      if (rfPrediction.getTargets() == null) {
        continue;
      }

      List<String> listNames = rfPrediction.getTargets().stream()
          .map(RfPredictionTarget::getTarget)
          .filter(t -> t.getType().equals(TargetType.PLACEMENT_CATEGORY.toString()))
          .map(t -> (PlacementCategory)t)
          .map(PlacementCategory::getPlacementCategoryListName)
          .collect(Collectors.toList());

      // 対応するプレイスメントカテゴリリストが存在しないrfPredictionIdはスキップ
      if (listNames.isEmpty()) {
        continue;
      }

      // rfPredictionIdに複数のプレイスメントカテゴリリストは紐づかない
      if (listNames.size() > 1) {
        throw new RuntimeException(String.format("Multiple placementCategoryListNames exist. rfPredictionId: %d", rfPredictionId));
      }

      // 対応するプレイスメントカテゴリリスト名が空やnullでなければ値を返却する
      String listName = listNames.get(0);
      if (!StringUtils.isEmpty(listName)) {
        return listName;
      }
    }
    return null;
  }
}
