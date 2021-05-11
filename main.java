public class main {
    public static void main(String[] args) {
        double Bigtime = 0.0; // this is the simulation main clock in seconds
        double Eventtime, EventtimeforNS, EventtimeforEW;// this is the event time
        double deltime;
        double time;
        // Event mananger EventQue and Que Simulation manager queMgr
        GenericManager<Event> EventQue = new GenericManager<Event>();
        //Now create an a queue of folks waiting for service.
        GenericManager<Car> NSroad = new GenericManager<>();
        GenericManager<Car> EWroad = new GenericManager<>();
        int Myid = 0;//this is the unique id
        int numinque = 0, numNSinQue = 0, numEWinQue = 0;
        int numinevent;// number of events in the event queue
        int totalthruline = 0, EWtotalthruline = 0, NStotalthruline = 0;
        double NStotalinLine=0,EWtotalinLine=0,avgNSinLine=0,avgEWinLine=0,counter =0;
        int NSCarsThru = 0, EWCarsThru = 0;
        double NSTotalTimeInLine = 0.0, EWTotalTimeInLine = 0.0;
        double totaltimeinservers = 0.0, totaltimeinservers2 = 0.0, ttil, ttis;
        
        boolean busy1 = false;//this is server 1
        boolean busy2 = false; // this is server 2.
        // starting with with this light but our light event occurs at 0 mins in time
        boolean NSLightGreen = false, EWLightGreen = true, NSLeftTurnArrowGreen = false;
        Car served1 = new Car(-9);//this is the car in server1
        Car served2 = new Car(-9);//this is the car in server2
        double deltimeserv;
        double timearrive, deltimearv;
        Car newcar = new Car(-9);//this is a work car object for those entereing the queue
        Car workcar = new Car(-9);//this is a work car object for those coming from the queue
        // now create the last event of the simulation
        Event workevent = new Event(8, 60000.0, 0); // 1000 hours or 60,000 mins
        //add the event in the queue
        numinevent = EventQue.addinorder(workevent);
        Event Lightevent = new Event(7, 0, 0);
        EventQue.addinorder(Lightevent);
        // debug var
        int carcounter = 0;
        int MaxNS = 0, MaxEW = 0;
        
        //now add the arrival for the first car from NS
        deltimearv = TimetoArriveorServe(30 / 15.0);//cars arrive at the rate of 30/15 min
        //the event time is current time plus the delta time
        Eventtime = Bigtime + deltimearv;
        // System.out.println("First car on NS arrives at " +Eventtime);
        EventtimeforNS = Eventtime;
        workevent = new Event(1, Eventtime, 0);
        //Store this event on the queue
        numinevent = EventQue.addinorder(workevent);
        // now car arrives for EW
        deltimearv = TimetoArriveorServe(30 / 15.0);//cars arrive at the rate of 30/15 min
        Eventtime = Bigtime + deltimearv;
        // System.out.println("First car on EW arrives at " +Eventtime);
        EventtimeforEW = Eventtime;
        workevent = new Event(2, Eventtime, 0);
        numinevent = EventQue.addinorder(workevent);
        //now start processing the events.
        // get the first event off of the event queue
        workevent = EventQue.getvalue(0);
        
        // While Loop
        while (workevent.getEtype() != 8) {
        deltime = workevent.getTime() - Bigtime;
        if (MaxEW < EWroad.mcount) {
        MaxEW = EWroad.mcount;
        }
        if (MaxNS < NSroad.mcount) {
        MaxNS = NSroad.mcount;
        }
        // Count up cars in line for statistics.
        NStotalinLine+=NSroad.mcount;
        EWtotalinLine+=EWroad.mcount;
        counter++;
        
        ttil = UpdateCars(NSroad, deltime);
        NSTotalTimeInLine += ttil;
        ttil = UpdateCars(EWroad, deltime);
        EWTotalTimeInLine += ttil;
        // now update the server
        ttis = UpdateServers(served1, busy1, served2, busy2, deltime);
        // now get the event type and process it. First update the time
        totaltimeinservers += ttis;
        totaltimeinservers2 += ttis * ttis;
        Bigtime = workevent.getTime();
        
        // BIG SWITCH CASE: Statement
        switch (workevent.getEtype()) {
        
        case 1://NS Car arrives at intersection.
        carcounter++;
        // generate the object for the car
        Boolean turning = boolNSCarTurning();
        Boolean onComingTraffic = boolOncomingTraffic();
        if (NSLightGreen && NSLeftTurnArrowGreen) { // Green light on NS and car is from NS
        // create the car object
        // System.out.println("NS Car arrives");
        newcar = new Car(-9);
        //set the arrival time for this car
        newcar.SetArrive(Bigtime);
        // Generate finished server event for car
        // is this car turning?
        if (turning) {
        time = TimetoArriveorServe(1 / 8.0); // 1 turn per 8 seconds
        deltimeserv = time / 60.0;
        } else {
        time = TimetoArriveorServe(1 / 4.0);
        deltimeserv = time / 60.0;
        }
        Eventtime = deltimeserv + Bigtime;
        workevent = new Event(5, Eventtime, -9);
        numinevent = EventQue.addinorder(workevent);
        NSCarsThru++;
        } else if (NSLightGreen && !NSLeftTurnArrowGreen) {// Green light on NS and car is from NS but NO green left turn signal

        // is this car turning?
        if ((turning == true) && (onComingTraffic == false)) {
        // we are turning and there is no traffic
        newcar = new Car(-9);
        //set the arrival time for this car
        newcar.SetArrive(Bigtime);
        time = TimetoArriveorServe(1 / 8.0); // 1 turn per 8 seconds
        deltimeserv = time / 60.0;
        Eventtime = deltimeserv + Bigtime;
        workevent = new Event(5, Eventtime, -9);
        numinevent = EventQue.addinorder(workevent);
        NSCarsThru++;
        } else if ((turning == true) && (onComingTraffic == true)) {
        // we are trying to turn but there is traffic
        // System.out.println("NS Car goes into the line");
            Myid++;
            newcar = new Car(Myid);
            
        //set the arrival time for this car
            newcar.SetArrive(Bigtime);
            newcar.type = 1; // car is turning Left
            NSroad.addatend(newcar);
        } else if (turning == false) {
            time = TimetoArriveorServe(1 / 4.0); // 2 straight
            deltimeserv = time / 60.0;
            Eventtime = deltimeserv + Bigtime;
            workevent = new Event(5, Eventtime, -9);
            numinevent = EventQue.addinorder(workevent);
            NSCarsThru++;
        }
        } else if (!NSLightGreen) {//Its a NS Event but car must wait bc RED light
        //first generate the car, note this car must have a unique ID
        // System.out.println("NS Car goes into the line");
        Myid++;
        newcar = new Car(Myid);
        // set the arrival time for this car
                newcar.SetArrive(Bigtime);
        if (turning == true) {
                newcar.type = 1; // car is turning Left
            } else {
                newcar.type = 2; // car is going straight
            }
            NSroad.addatend(newcar);
        } // car is sitting at the light
        //the car is in the line
        //generate the event for the next car to arrive
            deltimearv = TimetoArriveorServe(30 / 15);//cars arrive at the rate of 30/15 min
        //the event time is current time plus the delta time
            Eventtime = Bigtime + deltimearv;
        // System.out.println("Nextcar on NS arrives at " + Eventtime);
            EventtimeforNS = Eventtime;
            workevent = new Event(1, Eventtime, 0);
        //Store this event on the queue
            numinevent = EventQue.addinorder(workevent);
        break;
        case 2: //Event for EW
        carcounter++;
        Boolean ewturning = boolEWCarTurningDirection();
        if (EWLightGreen) {
        // System.out.println("EW Car arrives");
        newcar = new Car(-9);
        //set the arrival time for this car
        newcar.SetArrive(Bigtime);
        // Generate finished server event for car
        // is this car turning?
        if (ewturning) {
        
        time = TimetoArriveorServe(1 / 5.0); // turning south
        deltimeserv = time / 60.0;
        } else {
        time = TimetoArriveorServe(1 / 8.0); // turning north
        deltimeserv = time / 60.0;
        }
        Eventtime = deltimeserv + Bigtime;
        workevent = new Event(5, Eventtime, -9);
        numinevent = EventQue.addinorder(workevent);
        EWCarsThru++;
        } else {
        // System.out.println("EW Car goes into the line");
        Myid++;
        newcar = new Car(Myid);
        //set the arrival time for this car
        newcar.SetArrive(Bigtime);
        if (ewturning) {
        newcar.type = 1; // turning north
        } else {
        newcar.type = 2; // turning south
        }
        EWroad.addatend(newcar);
        }
        deltimearv = TimetoArriveorServe(30 / 15);//cars arrive at the rate of 30/15 min
        //the event time is current time plus the delta time
        Eventtime = Bigtime + deltimearv;
        // System.out.println("Nextcar on NS arrives at " + Eventtime);
        EventtimeforEW = Eventtime;
        workevent = new Event(2, Eventtime, 0);
        //Store this event on the queue
        numinevent = EventQue.addinorder(workevent);
        
        break;
        case 3: // Car enters intersection from NS
        // decrement the number in line
        //generate completion time and departure event for this car
        numNSinQue = NSroad.getmcount();
        if (NSLightGreen && numNSinQue > 0) {
        // System.out.println("first NS car in line enters Intersection");
        workcar = NSroad.getvalue(0);
        NSCarsThru++;//this car just came out of line
        // delete this car from the queue and put them in the server.
        NSroad.removem(0);
        //put this car in intersection
        served1 = workcar;
        // generate the finished server event for this car
        if (workcar.type == 1) {
        time = TimetoArriveorServe(1 / 8.0);
        deltimeserv = time / 60.0;
        } else {
        
        time = TimetoArriveorServe(1 / 4.0);
        deltimeserv = time / 60.0;
        }
        Eventtime = deltimeserv + Bigtime;
        workevent = new Event(5, Eventtime, -9);
        //put this event in the event queue
        numinevent = EventQue.addinorder(workevent);
        }
        break;
        case 4: // Car enters intersection from EW
        // decrement the number in line
        //generate completion time and departure event for this car
        numEWinQue = EWroad.getmcount();
        if (EWLightGreen && numEWinQue > 0) {
        // System.out.println("first EW car in line enters Intersection");
        workcar = EWroad.getvalue(0);
        EWCarsThru++;//this car just came out of line
        // delete this car from the queue and put them in the server.
        EWroad.removem(0);
        //put this car in intersection
        served2 = workcar;
        // generate the finished server event for this car
        if (workcar.type == 1) {
        time = TimetoArriveorServe(1 / 5.0); // turning south
        deltimeserv = time / 60.0; // convert to minutes
        } else {
        time = TimetoArriveorServe(1 / 8.0); // turning north
        deltimeserv = time / 60.0; // convert to minutes
        }
        Eventtime = deltimeserv + Bigtime;
        workevent = new Event(6, Eventtime, -9);
        //put this event in the event queue
        numinevent = EventQue.addinorder(workevent);
        }
        break;
        case 5: // Car leave intersection from NS
        // update the number of cars through the system
        //if there are cars in line generate an enter service bay 1 event
        NStotalthruline++;
        numinque = NSroad.getmcount();
        if (numinque > 0) {//there are cars in the line, generate a car enter service bay 1 now at Bigtime
        //NOTE PROBLEMS WITH COLLISION EVENTS
        workevent = new Event(3, Bigtime + .01, -9);
        //put this event in the event queue
        numinevent = EventQue.addinorder(workevent);
        }
        break;
        
        case 6: // Car leaves intersection from EW
        EWtotalthruline++;
        numinque = EWroad.getmcount();
        if (numinque > 0) {
        workevent = new Event(4, Bigtime + .01, -9);
        numinevent = EventQue.addinorder(workevent);
        }
        break;
        case 7: // Light Event
        // check the front of the line
        if (NSLeftTurnArrowGreen) {
        numNSinQue = NSroad.getmcount();
        if (numNSinQue > 0) {
        workcar = NSroad.getvalue(0);
        if (workcar.type == 1) {
        // car turning
        time = TimetoArriveorServe(1 / 4.0); // 2 straight
        } else {
        // car is going straight
        time = TimetoArriveorServe(1 / 8.0); // 2 straight
        }
        deltimeserv = time / 60.0;
        Eventtime = deltimeserv + Bigtime;
        workevent = new Event(5, Eventtime, -9);
        numinevent = EventQue.addinorder(workevent);
        }
        NSLeftTurnArrowGreen = false;
        workevent = new Event(7, Bigtime + 2, -9);
        numinevent = EventQue.addinorder(workevent);
        // add event to to change NS light in 2 minutes to red
        } else {
        if (NSLightGreen) {
        numEWinQue = EWroad.getmcount();
        if (numEWinQue > 0) {
        workcar = EWroad.getvalue(0);
        if (workcar.type == 1) {
        // car turning
        
        time = TimetoArriveorServe(1 / 5.0); // 1 turning south
        
        } else {
        // car is going straight
        
        time = TimetoArriveorServe(1 / 8.0); // 2 turning north
        
        }
        deltimeserv = time / 60.0;
        Eventtime = deltimeserv + Bigtime;
        workevent = new Event(6, Eventtime, -9);
        numinevent = EventQue.addinorder(workevent);
        }
        NSLightGreen = false;
        EWLightGreen = true;
        // add event to change NS light and EW light in 2 mins
        workevent = new Event(7, Bigtime + 2, -9);
        numinevent = EventQue.addinorder(workevent);
        } else {
        if (EWLightGreen) {
        
        numNSinQue = NSroad.getmcount();
        if (numNSinQue > 0) {
        workcar = NSroad.getvalue(0);
        if (workcar.type == 1) {
        // car turning
        time = TimetoArriveorServe(1 / 8.0); // turning
        
        } else {
            // car is going straight
            time = TimetoArriveorServe(1 / 4.0); // straight
        }
                deltimeserv = time / 60.0;
                Eventtime = deltimeserv + Bigtime;
                workevent = new Event(5, Eventtime, -9);
                numinevent = EventQue.addinorder(workevent);
        }
                EWLightGreen = false;
                NSLeftTurnArrowGreen = true;
                NSLightGreen = true;
                // add event to change NS left turn light in 1 min
                workevent = new Event(7, Bigtime + 1, -9);
                numinevent = EventQue.addinorder(workevent);
                }
            }
        }
        break;
        case 8://this is the shutdown event
        System.out.println(" this event is type 8 and we are in the switch statement TROUBLE!");
        continue;
        default:
        System.out.println("this is a bad event type" + workevent.getEtype() + " at time " +
        workevent.getTime());
        }// end of the switch statement
        EventQue.removem(0);
      //  System.out.println("*************************the time is " + Bigtime +"****************************\"");
        workevent = EventQue.getvalue(0);
        } // end of our simulation loop
        
        // Statistics
        System.out.println(MaxEW + " EW MAX");
        System.out.println(MaxNS + " NS MAX");
        System.out.println("*****Printing the Statistics for this Run*****");
        System.out.println("There was a total of " + (NSCarsThru + EWCarsThru) + " cars");
        
        System.out.println("*****Statistics for NW Cars*****");
        System.out.println("There was a total of " + NSCarsThru);
        System.out.println("There was an AVG of " + NStotalinLine/counter +" vehicles waiting at the NS light");
        System.out.println("They spent an Avg time of " + NSTotalTimeInLine / NSCarsThru + " ");
        
        System.out.println("*****Statistics for EW Cars*****");
        System.out.println("There was a total of " + EWCarsThru);
        System.out.println("There was an AVG of " + EWtotalinLine/counter +" vehicles waiting at the EW light");
        System.out.println("They spent an Avg time of " + EWTotalTimeInLine / EWCarsThru + " ");
        
        } // end of main method
        public static void OutaHere(GenericManager<Car> CarLine, int id) {// this function removes a car from the Queue line CarLine. It traverces the line, finds the car with id
            // and removes them
            int i, numinline, Cid;
            Car workcar = new Car(-9);
            //prepare to trasverse the car line
            numinline = CarLine.getmcount();
            workcar = CarLine.getvalue(0);
            Cid = workcar.GetMyid();
            i = 0;
            try {
            while ((Cid != id) && (i <= (numinline - 1))) {
            workcar = CarLine.getvalue(i);
            Cid = workcar.GetMyid();
            // System.out.println("in OutaHere checking car"+i+"with id"+Cid+ "against id "+id);
            i++;
            }
            // removing car i from the line
            //System.out.println("removing Car with id"+ Cid+ "against id"+id);
            if (i == 0) {
            CarLine.removem(0);//we are removing the first car in line
            // System.out.println("removing the first car in line");
            } else if ((Cid == id) && (i > 0)) CarLine.removem(i - 1);
            } catch (Exception e) {
            System.out.println("Car Not here!");
            }
            return;
            }//end of OutaHere
            
        public static boolean boolNSCarTurning() {
                int x = (int) (Math.random() * 100);
                boolean turning = false;
                if (x <= 25) {
                turning = true; // Turning east
                } else {
                turning = false; // Continuing North
                }
                return turning;
                }
                public static boolean boolEWCarTurningDirection() {
                
                int x = (int) (Math.random() * 100);
                boolean Bdirection = false;
                if (x <= 70) {
                Bdirection = true; // Turning South
                } else {
                Bdirection = false; // Turning North
                }
                return Bdirection;
                }
                public static boolean boolOncomingTraffic() {
                // Method for NS traffic to see if we can turn left without a Green arrow
                int x = (int) (Math.random() * 100);
                boolean oncomingTraffic = false;
                if (x <= 50) {
                oncomingTraffic = true;
                } else {
                oncomingTraffic = false;
                }
                return oncomingTraffic;
                }

        public static double TimetoArriveorServe(double rate) 
                {//this is the ramdom process to determine the time to arrive or the service time. rate is the arrival or service rate.
                    double deltime;
                    double bigx;
                    bigx = Math.random();
                    if (bigx > 0.9) bigx = Math.random();
                    deltime = -Math.log(1.0 - bigx) / rate;
                    // System.out.println("in time to arrive with rate "+rate+" the del time is "+deltime+" bigx i"+bigx);
                    return deltime;
                }//this is the end of the random process generator for deltime
        public static double UpdateCars(GenericManager<Car> carline, double deltime) 
                        {// this function adds up all the time spent for a car in line for this deltime
                            double linetime = 0.0;
                            int carinline;
                            carinline = carline.getmcount();
                            if (carinline == 0)
                            return linetime;
                            else
                            return linetime = deltime * carinline;
                        }//end of UpdateCar
        public static double UpdateServers(Car s1, boolean b1, Car s2, boolean b2, double deltime)
                    {// this function updates the time to cars in the servers
                            double servetime = 0.0;
                            if (b1 && b2) return servetime = 2 * deltime;
                            else if (b1 || b2) servetime = deltime;
                            return servetime;
                    }//end of UpdateServers
    } // end of main Class