#!/usr/bin/env python3
import datetime
import time
import socket

HOST = '127.0.0.1'
PORT = 65432        # The port used by the server
RECV_BUFFER = 4096  # valor recomendado na doc. do python

def handle(s):
    while True:
        data = s.recv(RECV_BUFFER)
        print(data.decode())
        c = input("->")
        s.sendall(c.encode())
        if int(c)==1:
            data = s.recv(RECV_BUFFER)
            print(data.decode())
            #c = input("->")
            s.sendall(input("->").encode())
            data = s.recv(RECV_BUFFER).decode()
            print(data)
        elif int(c)==2:
            data = s.recv(RECV_BUFFER).decode()
            print(data)
        elif int(c)==3:
            data = s.recv(RECV_BUFFER).decode()
        elif int(c)==4:
            print("Closing Socket...")
            s.close()
            break

def make_connection(host, port, bufferSize):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host, port))
    buf = '3\n'
    s.sendall(buf.encode())
    handle(s)

make_connection(HOST, PORT, RECV_BUFFER)
