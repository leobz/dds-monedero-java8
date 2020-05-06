package dds.monedero.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class MonederoTest {
  private Cuenta cuenta;

  @Before
  public void init() {
    cuenta = new Cuenta();
  }

  @Test
  public void Poner() {
	double saldo_ingresado = 1500;
	double saldo_inicial = cuenta.getSaldo();
    cuenta.poner(saldo_ingresado);
    Assert.assertEquals(cuenta.getSaldo(), saldo_inicial + saldo_ingresado, 0);
  }

  @Test(expected = MontoNegativoException.class)
  public void PonerMontoNegativo() {
    cuenta.poner(-1500);
  }

  @Test
  public void TresDepositos() {
	double saldo_ingresado = 1500;
	double saldo_inicial = cuenta.getSaldo();
    cuenta.poner(saldo_ingresado);
    cuenta.poner(saldo_ingresado);
    cuenta.poner(saldo_ingresado);
    Assert.assertEquals(cuenta.getSaldo(), saldo_inicial + saldo_ingresado * 3 , 0);
  }

  @Test(expected = MaximaCantidadDepositosException.class)
  public void MasDeTresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    cuenta.poner(245);
  }

  @Test(expected = SaldoMenorException.class)
  public void ExtraerMasQueElSaldo() {
    cuenta.setSaldo(90);
    cuenta.sacar(1001);
  }

  @Test(expected = MaximoExtraccionDiarioException.class)
  public void ExtraerMasDe1000() {
    cuenta.setSaldo(5000);
    cuenta.sacar(1001);
  }

  @Test(expected = MontoNegativoException.class)
  public void ExtraerMontoNegativo() {
    cuenta.sacar(-500);
  }

}