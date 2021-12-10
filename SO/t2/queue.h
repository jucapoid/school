#include <stdbool.h>

typedef struct Queue               // Criação da queue com os respetivos valores:
{ 
    int head;                      // Índice do valor no inicio da queue
    int tail;                      // Índice do valor no final da queue
    int size;                      // Posições ocupadas na queue
    unsigned capacity;             // Capacidade máxima da queue
    int *array;                    // Array para servir de queue
}Queue;

struct Queue *new_queue(unsigned capacity); 
bool isFull(Queue *queue);
bool isEmpty(Queue *queue);
bool enqueue(Queue *queue, int item);
int dequeue(Queue *queue);
int size(Queue *queue);
void free_queue(Queue * queue);
bool isHead(Queue *queue, int pid);
