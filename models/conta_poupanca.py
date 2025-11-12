class ContaPoupanca(Conta):
    """Conta poupança simples"""
    
    def get_tipo(self) -> str:
        return "Conta Poupança"
