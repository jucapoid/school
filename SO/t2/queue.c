#include <stdio.h> 
#include <stdlib.h>
#include "queue.h"

/*
    Método para criar queue com capacidade máxima de capacity
    começando com um tamanho size = 0
*/

Queue *new_queue(unsigned capacity) 
{ 
    Queue *queue = malloc(sizeof(Queue)); 
    queue->capacity = capacity; 
    queue->head = 0;
    queue->size = 0;  
    queue->tail = capacity - 1 ;
    queue->array = malloc(capacity * sizeof(int)); 
    return queue; 
} 
  
/*
    Método para dizer se a queue está cheia
*/

bool isFull(Queue *queue) 
{
    return (queue->size == queue->capacity);
} 
  
/*
    Método para dizer se a queue está vazia
*/

bool isEmpty(Queue *queue) 
{
    return (queue->size == 0);
} 
  
/*
    Método para dar enqueue ao inteiro value
*/

bool enqueue(Queue *queue, int value) 
{ 
    if (isFull(queue))
    {
        return false;
    }
    queue->tail = (queue->tail + 1)%queue->capacity;
    queue->array[queue->tail] = value;
    queue->size++; 
    return true;
} 
  
/*
    Método para remover o inteiro no inicio do array e devolvê-lo
*/

int dequeue(Queue *queue) 
{ 
    if (isEmpty(queue))
    {
        return 0;
    } 
    int value = queue->array[queue->head]; 
    queue->head = (queue->head + 1)%queue->capacity; 
    queue->size--; 
    return value; 
}

int size(Queue *queue){
    return queue->size;
}

void free_queue(Queue * queue) {
	free(queue->array);
	free(queue);
}

bool isHead(Queue *queue, int pid) {
	if (queue->array[queue->head] == pid) {
		return true;
	}
	return false;
}
