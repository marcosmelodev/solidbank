class Transacao:
    """Representa uma transação bancária"""
    
    def __init__(self, tipo: str, valor: float, data_hora: datetime = None):
        self.tipo = tipo
        self.valor = valor
        self.data_hora = data_hora if data_hora else datetime.now()
    
    def __str__(self):
        simbolo = "+" if self.tipo == "DEPOSITO" else "-"
        return f"{self.data_hora.strftime('%d/%m/%Y %H:%M')} | {self.tipo:12} | {simbolo}R$ {self.valor:.2f}"