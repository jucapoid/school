#!/usr/bin/env python3
import datetime
import time
import socket

TIMEOUT = 10
HOST = '127.0.0.1'
PORT = 65432        # The port used by the server
ID = 2
TYPE = "NO2"
LOCATION = "Lisboa"
FIR_VER = 0.01
UNIT = "ppm"
READ_VALUE = 10000
RECV_BUFFER = 4096  # valor recomendado na doc. do python

def handle(s):
    while True:
        time.sleep(TIMEOUT)
        buf = str(ID) + '\n' + str(time.strftime('%c')) + '\n' + str(READ_VALUE)  + '\n' + UNIT + '\n' + str(FIR_VER) + '\n'
        s.sendall(buf.encode())
        data = s.recv(RECV_BUFFER)
        print(data.decode())
        continue

def make_connection(host, port, bufferSize):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host, port))
    buf = '1\n' + str(ID) + '\n' + TYPE + '\n' + LOCATION + '\n' + str(FIR_VER) + '\n'
    s.sendall(buf.encode())
    data = s.recv(bufferSize)
    print(data.decode())
    handle(s)

make_connection(HOST, PORT, RECV_BUFFER)
