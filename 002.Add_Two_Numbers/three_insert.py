# -*- coding: utf-8 -*-
"""
Created on Tue Aug 15 16:24:40 2017

@author: OrionDuo
"""
# This function is in linkedlist class
# function to insert a new node at the beginning
def push(self,new_data):
    # step 1&2 : allocate the node and put in the data
    new_node = Node(new_data)
    # step 3 : make next of new node as head
    new_node.next = self.head
    # step 4 : move the head to point to new node
    self.head = new_node

# this function is in likedlist class
# insert a new node after the given pre_node 
def insertAfter(self, prev_node, new_data):
    # step 1 : check if the given prev_node exists
    if prev_node is None:
        print("The given previous node must in linkedlist. ")
        return
    # step 2 and 3 : create new node and put in the data
    new_node = Node(new_data)
    # step 4 : make next of new nodeas next of prev_node
    new_node = prev_node.next
    # step 5 : make next of prev_node  as new_node
    prev_node = new_node

# this function is in likedlist class
# appending a new node at the end of the list
def Append(self, new_data):
    # step  1 and 2 : create a new node and put in the data
    # step 3 : set next node as None
    new_node = Node(new_data)
    # step 4 : ifthe liked list is empty, then make the new node as head
    if self.head is None
        self.head = new_node
        return
    # step 5 : else traverse till the last node
    while(last.next):
        last = last.next
    # step 6 : change the next of last node
    last.next = new_node
    