#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import socket, select, traceback, os, time

my_host = '127.0.0.1'  # Standard loopback interface address (localmy_host)
my_port = 65432        # my_port to listen on (non-privileged my_ports are > 1023)
bufferSize = 4096  # valor recomendado na doc. do python

SOCK_LIST = []
SENSOR_LIST = []
CLIENT_LIST = []
ADMIN_LIST = []

def handleAdmins(socks, svSock):
    for sock in socks:
        if sock.fileno() < 0:
            SENSOR_LIST.remove(sock)

        if sock != svSock:
            data = sock.recv(RECV_BUFFER)
            if not data: # não há dados, o cliente fechou o socket
                print("Client disconnected")
                sock.close()
                SOCK_LIST.remove(sock)
            else: # há dados
                c = int(data.decode())
                if c==1:
                    sock.sendall("Escolha o sensor que deseja pelo ID\n".encode())
                    data = sock.recv(RECV_BUFFER).decode()
                    sock.sendall("sensor %s\n" % (char(data)).encode())
                elif c==2:
                    buf = str(SENSOR_LIST) + "\nMENU\n1:Ultima Leitura sensor x\n2:Listar sensores\n3:Firmware Update\n4:Disconnect\n"
                    sock.sendall(buf.encode())
                elif c==3:
                    sock.sendall("Development yet\nMENU\n1:Ultima Leitura sensor x\n2:Listar sensores\n3:Firmware Update\n4:Disconnect\n".encode())
                elif c==4:
                    print("Client disconnected")
                    sock.close()
                    ADMIN_LIST.remove(sock)
                else:
                    sock.sendall("MENU\n1:Ultima Leitura sensor x\n2:Listar sensores\n3:Firmware Update\n4:Disconnect\n".encode())


def handleClients(socks, svSock):
    for sock in socks:
        if sock.fileno() < 0:
            SENSOR_LIST.remove(sock)

        if sock != svSock:
            data = sock.recv(RECV_BUFFER)
            if not data: # não há dados, o cliente fechou o socket
                print("Client disconnected")
                sock.close()
                SOCK_LIST.remove(sock)
            else: # há dados
                sock.sendall("Thanks\n".encode())


def handleSensors(socks, svSock):
    for sock in socks:
        if sock.fileno() < 0:
            SENSOR_LIST.remove(sock)

        if sock != svSock:
            data = sock.recv(RECV_BUFFER)
            if not data: # não há dados, o cliente fechou o socket
                print("Client disconnected")
                sock.close()
                SOCK_LIST.remove(sock)
            else: # há dados
                print(data.decode())
                sock.sendall("Thanks\n".encode())


def chooseClient(conn):
    data = conn.recv(bufferSize)
    typ = data.decode().split()[0]

    if not data:
        print("Client disconnected")
        conn.close()
    elif typ == "sensor":
        print(data.decode() + '\n')
        SENSOR_LIST.append(conn)
        SOCK_LIST.remove(conn)
        conn.sendall("Registered as a Sensor\n".encode())
    elif typ == "public":
        print("Registered as Public Client" + '\n')
        CLIENT_LIST.append(conn)
        SOCK_LIST.remove(conn)
        conn.sendall("MENU\n1:Listar locais com medição de CO2\n2:Leitura de Local especifico\n".encode())
    elif typ == "admin":
        print("Registered as an Admin" + '\n')
        ADMIN_LIST.append(conn)
        conn.sendall("MENU\n1:Ultima Leitura sensor x\n2:Listar sensores\n3:Firmware Update\n4:Disconnect\n".encode())


if __name__ == "__main__":
    print("\nInitializing broker socket...")
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((my_host, my_port))  # aceita ligações de qualquer lado
    server_socket.listen(10)
    server_socket.setblocking(0) # o socket deixa de ser blocking
    print("\nBroker Listening for connections")

    # Adicionamos o socket à lista de sockets a monitorizar
    SOCK_LIST.append(server_socket)

    timecount = 0
    while True:  # ciclo infinito

        # apagamos os sockets que "morreram" entretanto
        for sock in SOCK_LIST:
            if sock.fileno() < 0:
                SOCK_LIST.remove(sock)

        rsocks,_,_ = select.select(SOCK_LIST,[],[], 5)

        if len(rsocks) == 0: # timeout
            timecount += 5
            print("Timeout on select() -> %d secs" % (timecount))
            if timecount % 60 == 0:  # passou um minuto
                timecount = 0
            continue

        for sock in rsocks:  # percorrer os sockets com nova informação

            if sock == server_socket: # há uma nova ligação
                newsock, addr = server_socket.accept()
                newsock.setblocking(0)
                SOCK_LIST.append(newsock)

                print("New client - %s" % (addr,))
                chooseClient(newsock)

            else: # há dados num socket ligado a um cliente
                try:
                    data = sock.recv(bufferSize)
                    if data: 
                        chooseClient(newsock, bufferSize,)
                        
                    else: # não há dados, o cliente fechou o socket
                        print("Client disconnected 1")
                        sock.close()
                        SOCKET_LIST.remove(sock)
                        
                except Exception as e: # excepção ao ler o socket, o cliente fechou ou morreu
                    print("Client disconnected")
                    print("Exception -> %s" % (e,))
                    print(traceback.format_exc())
                    
                    sock.close()
                    SOCKET_LIST.remove(sock)

        sensorSocks,_,_ = select.select(SENSOR_LIST,[],[],5)
        handleSensors(sensorSocks, server_socket)
        clientSocks,_,_ = select.select(CLIENT_LIST,[],[],5)
        handleClients(clientSocks, server_socket)
        adminSocks,_,_ = select.select(ADMIN_LIST,[],[],5)
        handleAdmins(adminSocks, server_socket)