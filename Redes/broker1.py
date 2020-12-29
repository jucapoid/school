#!/usr/bin/env python3

import socket
import os
import time

TIMEOUT = 1000
HOST = '127.0.0.1'  # Standard loopback interface address (localhost)
PORT = 65432        # Port to listen on (non-privileged ports are > 1023)
FIR_VER = 0.01



def connection(conn, addr):
    newpid = os.fork()
    if(newpid == 0):
        print('Connected by', addr)
        with conn:
            recv = "".encode() #blank declaration
            recv = recv + conn.recv(1024) #making our full message
            received = recv.decode()
            print(received)
            if not recv:
                pass
            conn.sendall("Thanks Data Received".encode())

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    conn, addr = s.accept()
    while True:
        connection(conn, addr)
    s.close()
