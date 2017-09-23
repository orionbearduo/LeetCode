# Longest Substring Without Repeating Characters

## Problem Description

### Given a string, find the length of the longest substring without repeating characters.

*   Examples:

*   Given "abcabcbb", the answer is "abc", which the length is 3.

*   Given "bbbbb", the answer is "b", with the length of 1.

*   Given "pwwkew", the answer is "wke", with the length of 3. Note that the answer must be a substring, "pwke" is a subsequence and not a substring.

## Method

### To solve this problem, Hash table is the first data structure in my mind. Python provide the Dictionary same as the function of hash.

### Take "pwwkew" as an example. We need  a pointer: p-&gt; 0; an index: index-&gt;0,p; and the maxLen of the substring: maxLen-&gt; pointer + 1(why plus 1: when index and pointer all are 0, actually the string already has one element)

### The process:

*   Check the dictionary whether it contains the char or not. If exists, update the pointer.
*   Calculate the maxLen
*   Update the Dictionary