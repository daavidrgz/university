package e4;

public class TrafficJunction {
    private final trafficLights NORTH;
    private final trafficLights SOUTH;
    private final trafficLights EAST;
    private final trafficLights WEST;

    /**
    * Creates a trafic junction with four traffic lights named north , south ,
    * east and west . The north traffic light has just started its green cycle .
    */
    public TrafficJunction(){
        NORTH = trafficLights.NORTH;
        SOUTH = trafficLights.SOUTH;
        EAST = trafficLights.EAST;
        WEST = trafficLights.WEST;
    }

    public void timesGoesBy(){
        /**
        * Indicates that a second of time has passed , so the traffic light with
        * the green or amber light should add 1 to its counter . If the counter
        * passes its maximum value the color of the traffic light must change .
        * If it changes to red then the following traffic light changes to green .
        * The order is : north , south , east , west and then again north .
        */

        if(SOUTH.getColour().equals("RED") && EAST.getColour().equals("RED") && WEST.getColour().equals("RED")){
            NORTH.incrementTime();
        }
        if(NORTH.getColour().equals("RED") && EAST.getColour().equals("RED") && WEST.getColour().equals("RED")){
            SOUTH.incrementTime();
        }
        if(NORTH.getColour().equals("RED") && SOUTH.getColour().equals("RED") && WEST.getColour().equals("RED")){
            EAST.incrementTime();
        }
        if(NORTH.getColour().equals("RED") && SOUTH.getColour().equals("RED") && EAST.getColour().equals("RED")){
            WEST.incrementTime();
        }
    }
    /**
    * If active is true all the traffic lights of the junction must change to
    * blinking amber ( meaning a non - controlled junction ).
    * If active is false it resets the traffic lights cycle and started again
    * with north at green and the rest at red .
    * @param active true or false
    */

    public void amberJunction(boolean active){
        if(active){
            NORTH.setAmber();
            SOUTH.setAmber();
            EAST.setAmber();
            WEST.setAmber();
        }else{
            NORTH.setDefaults();
            SOUTH.setDefaults();
            EAST.setDefaults();
            WEST.setDefaults();
        }
    }
    /**
    * Returns a String with the state of the traffic lights .
    * The format for each traffic light is the following :
    * - For red colors : "[ name : RED ]"
    * - For green colors : "[ name : GREEN counter ]"
    * - For yellow colors with blink at OFF : "[ name : YELLOW OFF counter ]
    * - For yellow colors with blink at ON : "[ name : YELLOW ON ]
    * Examples :
    * [ NORTH : GREEN 2][ SOUTH : RED ][ EAST : RED ][ WEST : RED ]
    * [ NORTH : AMBER OFF 5][ SOUTH : RED ][ EAST : RED ][ WEST : RED ]
    * [ NORTH : AMBER ON ][ SOUTH : AMBER ON ][ EAST : AMBER ON ][ WEST : AMBER ON ]
    * @return String that represents the state of the traffic lights
    */

    @Override
    public String toString(){
        StringBuilder aux = new StringBuilder();
        for(trafficLights n : new trafficLights[]{this.NORTH, this.SOUTH, this.EAST, this.WEST}){
            aux.append(n.toString());
        }
        return aux.toString();
    }

}
