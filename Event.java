public class Event implements Comparable {
    /* This is the event class. Events hold an event type, an event time and in the case of a balk
event, a pointer to Car when the event is a balk event.*/
protected int x;// event type
protected double time;// this is the time of the event
protected int MyCar;//if this a balk event, this a unique identifier of the balking Car;
protected int etype;//this is the event type
public Event(int etype, double etime, int balkCar)
{ x=etype;
time=etime;
if(x==7)
{// this is a balk event
    MyCar=balkCar;
}
else
{// this is not a balk event
    MyCar=-9;
}
}//end of Event constructor
public int compareTo(Object o){
    if(getTime()>((Event)o).getTime())return 1;// if time a > time b return 1
    else
    if(getTime()<((Event)o).getTime())return -1;//if time a < time b return -1
    else return 0;
}//end of compareTo
public double getTime(){return time;}
public int getEtype(){return x;}
public int getMyCar(){return MyCar;}
}//end of class event
