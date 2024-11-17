package main.java.es.grise.upm.profundizacion.mocking.exercise2;

import es.grise.upm.profundizacion.mocking.exercise2.EngineController;
import es.grise.upm.profundizacion.mocking.exercise2.GearValues;
import es.grise.upm.profundizacion.mocking.exercise2.Gearbox;
import es.grise.upm.profundizacion.mocking.exercise2.Logger;
import es.grise.upm.profundizacion.mocking.exercise2.Speedometer;
import es.grise.upm.profundizacion.mocking.exercise2.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class EngineControllerTest {

    //SUT
    private EngineController engineController;

    private Logger logger;
    private Speedometer speedometer;
    private Gearbox gearbox;
    private Time time;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //Configuracion del estado inicial antes de cada prueba.
    @BeforeEach
    public void setup(){
        this.logger = mock(Logger.class);
        this.speedometer = mock(Speedometer.class);
        this.gearbox = mock(Gearbox.class);
        this.time = mock(Time.class);

        //SUT con collaborators mockeados
        engineController = new EngineController(logger,speedometer,gearbox,time);
    }

    //Comprueba que el mensaje log tiene el formato correcto
    @Test
    void testRecordGear() {

        // Se configura el mock TIME para que devuelva un timestamp actual
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        when(time.getCurrentTime()).thenReturn(currentTime);

        //Se llama al recordGear
        GearValues testGear = GearValues.FIRST;
        engineController.recordGear(testGear);

        //Comprueba que el mensaje que se espera del log sea igual al de la clase logger
        String expectedLog = sdf.format(currentTime) + " Gear changed to " + testGear;
        verify(this.logger).log(eq(expectedLog));
        
    }

    //Comprueba que el metodo getInstantaneousSpeed devuelve la velocidad correcta
    @Test
    void testGetInstantaneousSpeed(){
        
        //Se configura el mock SPEEDOMETER para que devuelva un valor de velocidad
        double[] speeds = {10.0,20.0,30.0};
        when(speedometer.getSpeed()).thenReturn(speeds[0]).thenReturn(speeds[1]).thenReturn(speeds[2]);

        //Se llama al getInstantaneousSpeed
         double speed = engineController.getInstantaneousSpeed();
        //Comprueba que el metodo getSpeed se ha llamado 3 veces
        verify(speedometer,times(3)).getSpeed();

        //Comprueba que la velocidad devuelta es la media de las 3 velocidades
        double expectedSpeed = (speeds[0]+speeds[1]+speeds[2]) / 3;

        //Comprueba que la velocidad devuelta es la esperada
        assertEquals(expectedSpeed, speed, 0.001);

    }

    //Invoca tres veces el metodo getInstantaneousSpeed
    @Test
    void testAdjust(){
        //para que no devuelva null
        when(speedometer.getSpeed()).thenReturn(10.0);
        when(time.getCurrentTime()).thenReturn(new Timestamp(0));

        engineController.adjustGear();
        Mockito.verify(speedometer, times(3)).getSpeed();

    }

    //El método adjustGear loguea la nueva marcha de recordGear
    @Test
    void testNuevaMarcha(){
        //Se configura el mock SPEEDOMETER para que devuelva un valor de velocidad
        double[] speeds = {10.0,20.0,30.0};
        when(speedometer.getSpeed()).thenReturn(speeds[0]).thenReturn(speeds[1]).thenReturn(speeds[2]);

        //Se configura el mock TIME para que devuelva un timestamp actual
        when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));     
    
        //Se llama al adjustGear
        engineController.adjustGear();

        // Verifica que el método recordGear se haya llamado
        verify(logger, times(1)).log(anyString());
    }

    @Test
    void testSetGear(){

        //Se configura el mock SPEEDOMETER para que devuelva un valor de velocidad
        double[] speeds = {10.0,20.0,30.0};
        when(speedometer.getSpeed()).thenReturn(speeds[0]).thenReturn(speeds[1]).thenReturn(speeds[2]);

        //Se configura el mock TIME para que devuelva un timestamp actual
        when(time.getCurrentTime()).thenReturn(new Timestamp(System.currentTimeMillis()));     
    
        //Se llama al adjustGear
        engineController.adjustGear();

        // Verifica que el método setGear se haya llamado con el valor correcto
        verify(gearbox, times(1)).setGear(GearValues.FIRST);
    }
}
