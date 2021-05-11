public class Car implements Comparable{
protected double timeNline;
protected double timeNserver;
protected double timeNsystem;
protected double timeArrive;
protected int mynum;
protected int Myid;//this is the unique identifier of my iding event
protected int type; // 1 = turning 2 = straight for NS or 1 = turning south and 2 turning north for EW
public Car(int x)
{ // create the patient object.
    timeNline=timeNserver=timeNsystem=0;
    mynum=x;
    Myid=x;
    type = 0;
}
public int compareTo(Object o){//the Patient class must have a comparable if we are to use in the queue manager
    if(GetNline()>((Car)o).GetNline())return 1;// if time a > time b return 1
    else
    if(GetNline()<((Car)o).GetNline())return -1;//if time a < time b return -1
    else return 0;
}//end of compareTo
public void setType(int Ctype)
{
    type = Ctype;
} // end of setType;
public void SetArrive(double x){
//the time we arrive is set at directly from x
    timeArrive=x;
} // end of SetArrive
public void SetNline(double x){
//note that we add the value of x it is the del time
    timeNline+=x;
} // end of SetNline
public void SetNserver(double x){
//note that we add the value of x it is the del time
    timeNserver+=x;
} // end of SetNserver
public void SetNsystem(){timeNsystem=timeNline+timeNserver;}
public void Setid(int x){
// x is the id event
    Myid=x;
}// end of SetID
public double GettimeArrive(){return timeArrive;}
public double GetNline(){return timeNline;}
public double GetNserver(){return timeNserver;}
public double GetNsystem(){return timeNsystem;}
public int GetMyid(){return Myid;}
    
}
