class Saque(Transacao):
    """Transação de saque"""
    
    def __init__(self, valor: float):
        self._valor = valor
        self.data_hora = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    
    @property
    def valor(self) -> float:
        return self._valor
    
    def registrar(self, conta: Conta):
        """Registra o saque na conta"""
        if conta.sacar(self._valor):
            conta.historico.adicionar_transacao(self)