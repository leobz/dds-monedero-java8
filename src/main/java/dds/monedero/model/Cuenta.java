package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (cantidadDeDepositos() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    setSaldo(getSaldo() + cuanto); //Definir otro uso con Polimorfiscmo de Movimiento
    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto, true);
    agregarMovimiento(movimiento);
  }
  
  private long cantidadDeDepositos() {
	return this.getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count();
  }

  public void sacar(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    } // Codigo repetido > astraer en un metodo
    if (getSaldo() - cuanto < 0) { //abstraer condicion en un metodo
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());// abstraer en una funcion con la line de abajo
    double limite = 1000 - montoExtraidoHoy; //abstraer en una funcion
    
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    
    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto, true);
    setSaldo(getSaldo() - cuanto); //Definir otro uso con Polimorfiscmo de Movimiento
    agregarMovimiento(movimiento);
  }

  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha)) //Abstrer condicion en un metodo
        .mapToDouble(Movimiento::getMonto)
        .sum(); 
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
