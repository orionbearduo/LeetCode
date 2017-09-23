# -*- coding: utf-8 -*-
"""
Created on Thu Sep 21 23:54:04 2017

@author: ichita
"""
class Solution(object):
    def lengthOfLongestSubstring(self,s):
        tempDict = {}
        pointer = 0
        maxLen = 0
        for index, value, in enumerate(s):
            if value in tempDict:
                pointer = max(tempDict[value]+1, pointer)
            maxLen = max(index - pointer + 1, maxLen)
            tempDict[value] = maxLen
        return maxLen
    
def excute():
    sol = Solution()
    print(sol.lengthOfLongestSubstring("pwwkew"))

if __name__ == "__main__":
    excute()
