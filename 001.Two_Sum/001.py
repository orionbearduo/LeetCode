# -*- coding: utf-8 -*-
"""
Created on Sat Jul 29 01:18:54 2017

@author: ichita
Using Hashing(python dictionary)
"""

class Solution(object):
    def twoSum(self, nums, target):
        if len(nums) <= 1:
            return False
        buff_dict = {}
        for i in range(len(nums)):
            if nums[i] in buff_dict:
                return [buff_dict[nums[i]], i]
            else:
                buff_dict[target - nums[i]] = i
