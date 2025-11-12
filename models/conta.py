class Conta(ABC):
    """Classe abstrata para contas (Open/Closed Principle)"""
    
    AGENCIA_PADRAO = "0001"
    _contador = 1
    
    def __init__(self, cliente: Cliente, id_conta: int = None, numero: int = None, saldo: float = 0.0):
        self.id = id_conta
        self.agencia = Conta.AGENCIA_PADRAO
        self.numero = numero if numero else Conta._contador
        self.saldo = saldo
        self.cliente = cliente
        self.transacoes: List['Transacao'] = []
        
        if not numero:
            Conta._contador += 1
    
    @abstractmethod
    def get_tipo(self) -> str:
        pass
    
    def sacar(self, valor: float) -> bool:
        if valor <= 0:
            print("❌ Valor inválido!")
            return False
        
        if valor > self.saldo:
            print("❌ Saldo insuficiente!")
            return False
        
        self.saldo -= valor
        print(f"✅ Saque de R$ {valor:.2f} realizado com sucesso!")
        return True
    
    def depositar(self, valor: float) -> bool:
        if valor <= 0:
            print("❌ Valor inválido!")
            return False
        
        self.saldo += valor
        print(f"✅ Depósito de R$ {valor:.2f} realizado com sucesso!")
        return True
    
    def transferir(self, valor: float, conta_destino: 'Conta') -> bool:
        if self.sacar(valor):
            conta_destino.depositar(valor)
            print(f"✅ Transferência de R$ {valor:.2f} realizada!")
            return True
        return False
    
    def exibir_extrato(self):
        print("\n" + "="*50)
        print(f"EXTRATO - {self.get_tipo()}")
        print("="*50)
        print(f"Agência: {self.agencia}")
        print(f"Conta: {self.numero}")
        print(f"Titular: {self.cliente.nome}")
        print("-"*50)
        
        if not self.transacoes:
            print("Nenhuma transação registrada.")
        else:
            for transacao in self.transacoes:
                print(transacao)
        
        print("-"*50)
        print(f"Saldo atual: R$ {self.saldo:.2f}")
        print("="*50 + "\n")