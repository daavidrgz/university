package e4;

enum trafficLights{
    NORTH(0, "GREEN", "OFF"),
    SOUTH(0, "RED", "OFF"),
    WEST(0, "RED", "OFF"),
    EAST(0, "RED", "OFF");

    private int time;
    private String colour;
    private String state;

    trafficLights(int time, String colour, String state){
        this.time = time;
        this.colour = colour;
        this.state = state;
    }

    public void incrementTime(){
        switch(this.getColour()){
            case "GREEN":
                if(this.getTime() == 15){ this.colour = "AMBER"; this.time = 0; }
                else{ this.time = this.time + 1; }
                break;
            case "AMBER":
                if(this.state.equals("OFF")){ //We ensure that the traffic light isn't blinking
                    if(this.getTime() == 5){ this.colour = "RED"; this.time = 0; }
                    else{ this.time = this.time + 1; }
                }
                break;
            case "RED":
                this.colour = "GREEN";
                break;
        }
    }

    public void setDefaults(){
        if(this == trafficLights.NORTH){
            this.state = "OFF";
            this.time = 0;
            this.colour = "GREEN";
        } else{
            this.state = "OFF";
            this.time = 0;
            this.colour = "RED";
        }

    }

    public void setAmber(){
        this.colour = "AMBER";
        this.state = "ON";
        this.time = 0;
    }

    public String getColour(){
        return this.colour;
    }
    public int getTime(){
        return this.time;
    }
    public String getState(){
        return this.state;
    }

    @Override
    public String toString(){
        if(this.getColour().equals("AMBER")){
            if(this.getState().equals("OFF")){
                return String.format("[%s: %s %s %d]", this.name(), this.getColour(), this.getState(), this.getTime());
            }else{
                return String.format("[%s: %s %s]", this.name(), this.getColour(), this.getState());
            }
        }else if(this.getColour().equals("GREEN")){
            return String.format("[%s: %s %d]", this.name(), this.getColour(), this.getTime());
        }else{
            return String.format("[%s: %s]", this.name(), this.getColour());
        }
    }
}
