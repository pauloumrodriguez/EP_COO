.text
.globl main

main:
	li $v0, 5  # lê o número "a"
	syscall  # chama o sistema operacional
	sw $v0, intA  # guarda na memória (variável intA) o valor do registrador $v0
	
	li $v0, 5  # lê o número "b"
	syscall  # chama o sistema operacional
	sw $v0, intB  # guarda na memória (variável intB) o valor do registrador $v0
	
	lw $a3, noAchou  #inicializando a flag como falsa (não encaixa)
	lw $t0, intA  # puxa da memória para o registrador
 	lw $t1, intB  # puxa da memória para o registrador
	
	addi $sp, $sp, -12  # reserva espaço para três inteiros (12 bytes) na pilha, para preservar os estados de "a", "b" e a flag
	sw $t0, 0($sp)  #guarda na pilha $t0 na posição 0 - 4
	sw $t1, 4($sp)  #guarda na pilha $t1 na posição 4 - 8
	sw $a3, 8($sp)  #guarda na pilha $a3 na posição 8 - 12
	jal loop  # pulo para o while (loop)
	
	lw $t7, achou  #inicializa uma variável para poder comparar com a flag e determinar o desvio condicional
	beq $a3, $t7, ir  # verifica se encaixa, caso encaixe vai para o label "ir"
	
        la $a0, noEncaixa  # carrega para $a0 que não encaixa
	li $v0, 4  # imprimi a variavel noEncaixa, isso é "Não encaixa"
	syscall  # chama o sistema operacional
	j end  # pula para o label que finaliza o programa
	
ir:
        la $a0, encaixa  # carrega para $a0 que encaixa
	li $v0, 4  # imprimi a variável encaixa, isso é "Encaixa"
	syscall  # chama o sistema operacional 
	j end  # pula para o label que finaliza o programa
	
loop:
	li $t4, 10  # inicializa um registrador temporário com 10
	
	div $t0, $t4  # divide "a" por 10
	mfhi $t2  # move o resto da div para $t2
	
	div $t1, $t4  # divide "b" por 10
	mfhi $t3  # move o resto da div para $t3
	
	beq $t2, $t3, restoIgual  # verifica se os restos são iguais, isto é, se os últimos algarismos batem
	# se coincidir, vai para o label restoIgual				
	lw $a3, noAchou  # caso não seja igual, seta a flag como não achou
	jr $ra  # encerra o while (loop) e volta para a instrução seguinte ao jal
	
restoIgual:
	div $t1, $t4  # divide "b" por 10
	mflo $t1  # faz com que o quociente dessa conta vire o valor de "b", finalizando a análise do último dígito
	
	div $t0, $t4  # divide "a" por 10
	mflo $t0  # faz com que o quociente dessa conta vire o valor de "a", finalizando a análise do último dígito
	
	lw $a3, achou  # seta a flag como achou
	bgtz $t1, loop  # se b > 0, continua o loop
	jr $ra  # se for lido todo b, ter dado certo, ele volta para a instrução seguinte do jal

end:
	lw $t0, 0($sp)  # restaura o valor de $t0 da pilha
	lw $t1, 4($sp)  # restaura o valor de $t1 da pilha
	lw $a3, 8($sp)  # restaura o valor de $t1 da pilha
	addi $sp, $sp, 12  #libera o espaço reservado na pilha, incrementa $sp para liberar o espaço ocupado
	
	li $v0, 10  # envia o código de serviço 10 que é "exit", para encerrar o programa
	syscall  # chama o sistema operacional

.data  # declaração de variáveis
intA: .word 0
intB: .word 0
achou: .word 1
noAchou: .word 0
encaixa: .asciiz "encaixa"
noEncaixa: .asciiz "nao encaixa"