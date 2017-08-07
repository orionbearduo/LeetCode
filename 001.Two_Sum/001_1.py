# -*- coding: utf-8 -*-
"""
Created on Mon Aug  7 22:31:20 2017
the normal way
@author: ichita
"""
class Solution(object):
    def twoSum(self, nums, target):
        num = len(nums)
        for i in range(num):
            for j in  range(i + 1, num):
                if(nums[i] + nums[j] == target):
                    return [i, j]
        return []
