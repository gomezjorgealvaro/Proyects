package uva.poo.practica2;
/**
 * Implementación concreta de un vehículo de combustión (gasolina) del sistema EcoRiderCity.
 *
 * <p>Extiende a {@link Vehiculo} con funcionalidades:
 * <ul>
 *   <li>Solo puede aparcar en plazas <strong>sin cargador</strong>.</li>
 *   <li>Calcula su autonomía a partir de la capacidad del depósito (L) y el consumo medio (L/100 km).</li>
 *   <li>Calcula un coste de “llenado” tras un alquiler suponiendo un precio fijo por litro.</li>
 *   <li>Mantiene un número de ocupantes permitido en el rango [1, 5].</li>
 * </ul>
 * 
 *s
 * @author alvgome
 * @author quegome
 * @see Vehiculo
 * @see Plaza
 */
public class GasolineVehicle extends Vehiculo{

	private int capacidadDeposito;
	private double consumoMedio;
	private int numOcupantes;
	/**
     * Crea un vehículo de gasolina con marca, modelo, matrícula y consumo medio.
     *
     * <p>Inicializa por defecto capacidadDeposito a 45 litros.</p>
     *
     * id           identificador interno del vehículo (no se usa aquí; se establece con {@link #setId(int)})
     * @param marca        marca del vehículo (no vacía)
     * @param modelo       modelo del vehículo (no vacío)
     * @param matricula    matrícula (formato español: 4 dígitos + 3 letras mayúsculas)
     * @param consumoMedio consumo medio en L/100 km (estrictamente positivo)
     *
     * @throws IllegalArgumentException si alguna validación heredada de {@link Vehiculo} falla
     *                                  o si consumoMedio es menor o igual que cero.
     */
	public GasolineVehicle(String marca, String modelo, String matricula, double consumoMedio) {
		super(marca, modelo, matricula);
		this.capacidadDeposito = 45;
		setConsumoMedio(consumoMedio);
	}
	 /**
     *Para un vehículo de gasolina devuelve {@code true} únicamente si la plaza notiene cargador.
     *
     * @param plaza plaza candidata
     * @return {@code true} si la plaza no tiene cargador; {@code false} en caso contrario
     * @throws NullPointerException si la plaza es null
     */
	@Override
	public boolean plazaCompatible(Plaza plaza) {
		return !plaza.tieneCargador();
	}
	/**
     * Devuelve los km de autonomia de un vehiculo, cada vehiculo depende de la capacidad
	 * de deposito y el consumo medio
     *
     * <p>Fórmula: {@code autonomia_km = (capacidadDeposito / consumoMedio) * 100}.</p>
     *
     * @return autonomía estimada en kilómetros
     * @throws IllegalStateException si el ConsumoMedio no es válido 
     */
	@Override
	public double kmAutonomia() {
		return (capacidadDeposito/consumoMedio)*100;
	}
	 /**
      * Devuelve la fianza a pagar de cada vehiculo.
     *
     *
     * @param fianza fianza base fijada por la ciudad (estrictamente positiva)
     * @return fianza total a abonar
     * @throws IllegalArgumentException si la fianza es negativa
     */
	@Override
	public double getFianzaAPagar(double fianza) {
		
		if(fianza <= 0)
			throw new IllegalArgumentException("La fianza tiene que ser mayor que 0");
		
		double porcentaje = (5*fianza)/100;
		return fianza + (porcentaje*numOcupantes);
	}
	/**
     * Obtener el coste de llenado que se tiene que pagar en el alquiler del vehiculo
     *
     *
     * @param kmRecorridos kilómetros recorridos durante el alquiler (no negativos)
     * @return coste de llenado en euros
     * @throws IllegalArgumentException si los KMrecorridos son negativos
     */
	@Override
	public double obtenerCosteLlenado(double kmRecorridos) {
		
		if(kmRecorridos < 0)
			throw new IllegalArgumentException("Los kilometos recorridos no pueden ser negativos");
		
		double gasto = kmRecorridos*consumoMedio;
		double cantidadALlenar = capacidadDeposito-gasto;
		
		return 1.5*cantidadALlenar;
	}
	/**
     * Actualiza los contadores de kilómetros asegurando que la distancia no supera la autonomía disponible.
     *
     * @param distancia distancia recorrida en km (no negativa y menor o igual que autonomía actual)
     * @throws IllegalArgumentException si {@code distancia} es negativa o mayor que {@link #kmAutonomia()}
     */
	@Override
	public void actualizarContadores(double distancia) {
		if(distancia > this.kmAutonomia())
			throw new IllegalArgumentException("Este vehiculo no puede recorrer esa distancia");

		super.actualizarContadores(distancia);
	}
	/**
     * Establece el consumo medio del coche.
     *
     * @param consumoMedio consumo en km/h
     * @throws IllegalArgumentException si el consumoMedio es menor que cero
     */
	public void setConsumoMedio(double consumoMedio) {
		if(consumoMedio <= 0)
			throw new IllegalArgumentException("EL consumo medio no puede ser cero o negativo");
		
		this.consumoMedio = consumoMedio;
	}
	/**
     * Establece el número de ocupantes del coche.
     *
     * @param numOcupantes número de ocupantes entre 1 y 5
     * @throws IllegalArgumentException si el número de ocupantes está fuera del rango permitido
     */
	@Override
	public void setNumOcupantes(int numOcupantes) {
		if(numOcupantes < 1 || numOcupantes > 5) 
			throw new IllegalArgumentException("El numero de ocupantes del coche tiene que estar entre 1 y 5");
		
		this.numOcupantes = numOcupantes;
	}
	
	/**
	 * Devuelve el consumo Medio de un coche de gasolina en sus respectivas unidades
	 */
	@Override
	public double getConsumoMedio() {
		return consumoMedio;
	}
	
	/**
	 * Devuelve la capacidad de bateria de un coche gasolina
	 */
	@Override
	public double getCapacidadBateria() {
		return capacidadDeposito;
	}
	
	/**
     * Devuelve el número de ocupantes configurado.
     *
     * @return número de ocupantes
     */
	@Override
	public int getNumOcupantes() {
		return numOcupantes;
	}
	@Override
	public String toString() {
	    return "GasolineVehicle{" +
	           "marca='" + getMarca() + '\'' +
	           ", modelo='" + getModelo() + '\'' +
	           ", matricula='" + getMatricula() + '\'' +
	           ", consumoMedio=" + consumoMedio + "kWh/100km" +
	           ", ocupantes=" + numOcupantes +
	           '}';
	}
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false; 
	    GasolineVehicle that = (GasolineVehicle) o;
	    String m1 = this.getMatricula();
	    String m2 = that.getMatricula();
	    return m1 != null ? m1.equals(m2) : m2 == null;
	}
	@Override
    public int hashCode() {
	        return getMatricula().hashCode();
	}
	@Override
	protected Vehiculo clonDelObjeto() {
	    GasolineVehicle c = new GasolineVehicle(getMarca(), getModelo(), getMatricula(), this.consumoMedio);
	    c.setNumOcupantes(this.numOcupantes);
	    return c;
	}
	@Override
	protected void despuesDeLaCopia(Vehiculo src) {
	    super.despuesDeLaCopia(src);
	    GasolineVehicle s = (GasolineVehicle) src;
	    this.numOcupantes = s.numOcupantes;
	}




}
