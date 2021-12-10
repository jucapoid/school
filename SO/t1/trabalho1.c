#include <stdio.h> 
#include <stdlib.h> 
#include <stdbool.h>
#include <string.h>

#include "queue.h"

#define RRQUANTUM 3
#define VRRQUANTUM 3
#define MAX 100                       
#define EMPTY -1
#define FILENAME "input1.txt"

typedef struct process
{
    	int pid;
    	int count;
    	int entry;
    	int *instances;
    	int size;
    	int state;
	
}process;

process new_process(int pid, int instr, int processo[]) {
	process proc;
	proc.pid = pid;
	proc.count = 0;
	proc.entry = processo[0];
	proc.size = instr;
	proc.state = 0;
	proc.instances = malloc(instr * sizeof(int));
	for (int i=1; i < instr; i++) {
		proc.instances[i-1] = processo[i];
	}
	return proc;
}

void free_process(process  proc){
	free(proc.instances);
}

void fill(int array[], int size)
{
    	for(int i = 0; i < size+1; i++)
    	{
		array[i] = EMPTY;
    	} 
}

void rr(process all[], int num_proc, int num_inst) {
	Queue *ready = new_queue(num_inst);
	Queue *block = new_queue(num_inst);
    	int time=0;
    	int p = 0;
    	int inst=0;
    	bool r=true,b=true;
    	bool sinal_r=false,sinal_b=false;
    	int e=0;

    	printf("TEMPO\t||");
    	for(int i=0;i< num_proc;i++){
		printf("P%d\t||",i+1);
    	}
    	printf("\n");
	while(e<num_proc){
		printf("%d\t||", time);
		for(int i=0; i<num_proc; i++){
			switch(all[i].state){
				//Processo no estado inicial
				case 0:
					//Instante igual ao instante de entrada do processo
					if(time==all[i].entry) {
						printf("NEW\t||");
						all[i].state=1;
						enqueue(ready,all[i].pid);
					} else {
						printf("\t||");
					}
					break;
				//Processo no estado READY
				case 1:
					//Processo na head do ready e run livre
					if(r && isHead(ready,all[i].pid)) {
						printf("RUN\t||");
						all[i].instances[all[i].count]--;
						all[i].state=2;
						r=false;
						inst=1;
						//Instrução chegou ao final dos ciclos necessários
						if(all[i].instances[all[i].count] == 0) {
							all[i].count++;
							sinal_r=true;
							p=dequeue(ready);
							//Próxima instrução é 0 processo termina
							if(all[i].instances[all[i].count]==0){
								all[i].state=6;
							//Processo passa para block
							} else {
								all[i].state=3;
								enqueue(block,p);
							}
						}
					} else {
						printf("READY\t||");
					}
					break;
				//Processo no estado RUN
				case 2:
					all[i].instances[all[i].count]--;
					inst++;
					printf("RUN\t||");
					//Instrução chegou ao final dos ciclos necessários
					if(all[i].instances[all[i].count] == 0) {
						all[i].count++;
						sinal_r=true;
						p=dequeue(ready);
						//Próxima instrução é 0 processo termina
						if(all[i].instances[all[i].count]==0){
							all[i].state=6;
						//Processo passa para block
						} else {
							all[i].state=3;
							enqueue(block,p);
						}
					} else if (inst == RRQUANTUM) {
						p=dequeue(ready);
						enqueue(ready,p);
						all[i].state=1;
						sinal_r=true;
					}
					break;
				//Processo no estado BLOCKED
				case 3:
					if(b && isHead(block,all[i].pid)) {
						all[i].state=4;
						b=false;
						printf("BLOCK*||");
						all[i].instances[all[i].count]--;
						//Se já foram consumidos os ciclos em block
						if(all[i].instances[all[i].count] == 0) {
							all[i].count++;
							sinal_b=true;
							//Se a próxima instrução for 0 ou atingido o final do processo terminar
							if(all[i].instances[all[i].count]==0 || all[i].count == all[i].size ) {
								dequeue(block);
								all[i].state=6;
							//Retirar da block
							} else {
								p=dequeue(block);
								enqueue(ready,p);
								all[i].state=1;
							}
						}
					} else {
						printf("BLOCK\t||");
					}
					break;
				case 4:
					printf("BLOCK*||");

					all[i].instances[all[i].count]--;
					//Se já foram consumidos os ciclos em block
					if(all[i].instances[all[i].count] == 0) {
						all[i].count++;
						sinal_b=true;
						//Se a próxima instrução for 0 ou atingido o final do processo terminar
						if(all[i].instances[all[i].count]==0 || all[i].count == all[i].size ) {
							dequeue(block);
							all[i].state=6;
						//Retirar da block
						} else {
							p=dequeue(block);
							enqueue(ready,p);
							all[i].state=1;
						}
					}
					break;
				case 6:
					if(all[i].count!=-1){
						printf("EXIT\t||");
						all[i].count=-1;
						e++;
					} else {
						printf("\t||");
					}
					break;
			}
		}
		if(sinal_r){
			sinal_r=false;
			r=true;
		}
		if (sinal_b) {
			sinal_b=false;
			b=true;
		}
		printf("\n");
		time++;
	}
	free_queue(block);
	free_queue(ready);
}

void vrr(process all[], int num_proc, int num_inst) {
	Queue *ready = new_queue(num_inst);
	Queue *block = new_queue(num_inst);
	Queue *aux = new_queue(num_inst);
    	int time=0;
    	int p = 0;
    	int inst=0;
    	bool r=true,b=true,a=false;
    	bool sinal_r=false,sinal_b=false,sinal_a=false;
    	int e=0;

    	printf("TEMPO\t||");
    	for(int i=0;i< num_proc;i++){
		printf("P%d\t||",i+1);
    	}
    	printf("\n");
	while(e<num_proc){
		printf("%d\t||", time);
		for(int i=0; i<num_proc; i++){
			switch(all[i].state){
				//Processo no estado inicial
				case 0:
					//Instante igual ao instante de entrada do processo
					if(time==all[i].entry) {
						printf("NEW\t||");
						all[i].state=1;
						enqueue(ready,all[i].pid);
					} else {
						printf("\t||");
					}
					break;
				//Processo no estado READY
				case 1:
					//Processo na head do ready e run livre
					if(r && !a && isHead(ready,all[i].pid)) {
						printf("RUN\t||");
						all[i].instances[all[i].count]--;
						all[i].state=2;
						r=false;
						inst=1;
						dequeue(ready);
						//Instrução chegou ao final dos ciclos necessários
						if(all[i].instances[all[i].count] == 0) {
							all[i].count++;
							sinal_r=true;
							//Próxima instrução é 0 processo termina
							if(all[i].instances[all[i].count]==0){
								all[i].state=6;
							//Processo passa para block
							} else {
								all[i].state=3;
								enqueue(block,all[i].pid);
							}
						}
					} else {
						printf("READY\t||");
					}
					break;
				//Processo no estado RUN
				case 2:
					all[i].instances[all[i].count]--;
					inst++;
					printf("RUN\t||");
					//Instrução chegou ao final dos ciclos necessários
					if(all[i].instances[all[i].count] == 0) {
						all[i].count++;
						sinal_r=true;
						//Próxima instrução é 0 processo termina
						if(all[i].instances[all[i].count]==0){
							all[i].state=6;
						//Processo passa para block
						} else {
							all[i].state=3;
							enqueue(block,all[i].pid);
						}
					} else if (inst == RRQUANTUM) {
						enqueue(ready,all[i].pid);
						all[i].state=1;
						sinal_r=true;
					}
					break;
				//Processo no estado BLOCKED
				case 3:
					if(b && isHead(block,all[i].pid)) {
						all[i].state=4;
						b=false;
						printf("BLOCK*||");
						all[i].instances[all[i].count]--;
						//Se já foram consumidos os ciclos em block
						if(all[i].instances[all[i].count] == 0) {
							all[i].count++;
							sinal_b=true;
							p=dequeue(block);
							//Se a próxima instrução for 0 ou atingido o final do processo terminar
							if(all[i].instances[all[i].count]==0 || all[i].count == all[i].size ) {
								all[i].state=6;
							//Retirar da block
							} else {
								enqueue(aux,p);
								all[i].state=5;
								sinal_a=true;
							}
						}
					} else {
						printf("BLOCK\t||");
					}
					break;
				case 4:
					printf("BLOCK*||");

					all[i].instances[all[i].count]--;
					//Se já foram consumidos os ciclos em block
					if(all[i].instances[all[i].count] == 0) {
						all[i].count++;
						sinal_b=true;
						p=dequeue(block);
						//Se a próxima instrução for 0 ou atingido o final do processo terminar
						if(all[i].instances[all[i].count]==0 || all[i].count == all[i].size ) {
							all[i].state=6;
						//Retirar da block
						} else {
							enqueue(aux,p);
							all[i].state=5;
							sinal_a=true;
						}
					}
					break;
				case 5:
					//Processo na head do aux e run livre
					if(r && isHead(aux,all[i].pid)) {
						printf("RUN\t||");
						all[i].instances[all[i].count]--;
						all[i].state=2;
						r=false;
						inst=1;
						dequeue(aux);
						if(isEmpty(aux)){
							a=false;
						}
						//Instrução chegou ao final dos ciclos necessários
						if(all[i].instances[all[i].count] == 0) {
							all[i].count++;
							sinal_r=true;
							//Próxima instrução é 0 processo termina
							if(all[i].instances[all[i].count]==0){
								all[i].state=6;
							//Processo passa para block
							} else {
								all[i].state=3;
								enqueue(block,all[i].pid);
							}
						}
					} else {
						printf("AUX\t||");
					}
					break;
					
				case 6:
					if(all[i].count!=-1){
						printf("EXIT\t||");
						all[i].count=-1;
						e++;
					} else {
						printf("\t||");
					}
					break;
			}
		}
		if(sinal_r){
			sinal_r=false;
			r=true;
		}
		if (sinal_b) {
			sinal_b=false;
			b=true;
		}
		if (sinal_a) {
			sinal_a=false;
			a=true;
		}
		printf("\n");
		time++;
	}
	free_queue(block);
	free_queue(ready);
	free_queue(aux);
}

int **ler_ficheiro(FILE *input, int *num_inst, int *num_proc){
	int **prog;
	char *l=NULL;
	size_t len=0;
	char * temp;
	while(getline(&l, &len, input) != -1) {
		(*num_inst)=0;
		for(temp = strtok(l, " "); temp != NULL; temp = strtok(NULL, " ")){
			(*num_inst)++;
		}
		(*num_proc)++;
	}
	prog = malloc((*num_proc)*(*num_inst)*sizeof(int));
	for(int i=0; i<*num_proc;i++){
		prog[i] = malloc((*num_inst)*sizeof(int));
	}
	rewind(input);
	int i = 0, j = 0;
	while(getline(&l, &len, input) != -1) {
		j=0;
		for(temp = strtok(l, " "); temp != NULL; temp = strtok(NULL, " ")){
			prog[i][j] = atoi(temp);
			j++;
		}
		i++;
	}
	return prog;
}

int main(int argc, char *argv[]){
    	int num_proc=0,num_inst=0;
	int **programas;
	
	if(argc == 3) {
		if(strcmp(argv[2],"file")==0){
			char file_name[] = FILENAME;
		    	FILE *input = fopen(file_name, "r+");
			programas = ler_ficheiro(input, &num_inst, &num_proc);

		} else if(strcmp(argv[2],"hard")==0) {
			programas = malloc(10*5*sizeof(int));
			num_proc = 5;
			num_inst = 10;
    			int progs[5][10] = {
				{0,3,1,2,2,4,1,1,1,1},
		    		{1,2,4,2,4,2,0,0,0,0},
		    		{3,1,6,1,6,1,6,1,0,0},
		    		{3,6,1,6,1,6,1,6,0,0},
		    		{5,9,1,9,0,0,0,0,0,0}
    			};

			for(int i = 0; i<5; i++){
				programas[i] = progs[i];
			}
		}
	
    		process *all_processes = malloc(num_proc * sizeof(process));
	
	    	for(int i=0;i<num_proc;i++)
    		{
			all_processes[i] = new_process(i, num_inst, programas[i]);
	    	}
		free(programas);

		if (strcmp(argv[1], "vrr") == 0){
			printf("Virtual Round-Robin Scheduler\n");
			vrr(all_processes, num_proc, num_inst);
		} else if (strcmp(argv[1], "rr") == 0) {
	    		printf("Round-Robin Scheduler\n");
		    	rr(all_processes, num_proc, num_inst);
		} else {
			printf("Deve ser passado como argumento rr ou vrr\n");
		}
		for(int i=0; i<num_proc; i++){
			free_process(all_processes[i]);
		}
		free(all_processes);
	} else {
		printf("Número de argumentos inválido\n");
	}
}
