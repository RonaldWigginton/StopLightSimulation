import java.util.ArrayList;

public class GenericManager<T extends Comparable>{
protected ArrayList<T> mylist= new ArrayList<T>();
protected int mcount;
public GenericManager()
{// this is the generic constructor
mcount=0;//mcount is the next available value in array myarray
}
public int addatend(T x)//this places values at the end of myarray
{ mylist.add(mcount++,x);
return mcount;}
public int getmcount(){return mcount;}
public int addinorder(T x)
{int i;
// System.out.println(" in addinorder and adding an object with mcount"+mcount);
// this places the object from smaller to larger
if((mcount==0)||((x.compareTo(mylist.get(0)))==-1)||(x.compareTo(mylist.get(0))==0))
{//this is less than or equal to the first entry
mylist.add(0,x);
}
else
if((x.compareTo(mylist.get(mcount-1))==1)||(x.compareTo(mylist.get(mcount-1))==0))
{// x is greater than the last entry

mylist.add(mcount,x);
}
else
{// this object is greater than the first and less than the last
i=0;
while((i<mcount)&&(x.compareTo(mylist.get(i))==1))i++;
mylist.add(i,x);
}
// add one to mcount
mcount++;
// for(i=0;i<=mcount-1;i++)System.out.println("in mylist at"+i+"value is "+mylist.get(i));
// System.out.println("leaving addinorder mcount is "+mcount);
return mcount;
}// end of add in order
public int addatfront(T x)
{// add this object at the front of the list
mylist.add(0,x);
mcount++;
return mcount;
}
public T getvalue(int i)//this gets values from myarray
{ if (i<mcount)return mylist.get(i);
else
{//System.out.println("in getvalue trying to get a value "+i+" when the value of mcount is "+mcount);
return mylist.get(0);
}
}//end of getvalue
public void ManageAndSort() {/* This is a generic sort. It will sort anything that the
manager manages BUT the objects
being sorted must support the compareTo function*/
//this method will sort an array of Flat objects based on their CompareTo function
T xsave, ysave,a,b;
int isw=1,xlast=mylist.size();
while (isw==1)
{isw=0;
for(int i=0;i<=xlast-2;i++)
{a=mylist.get(i);
b=mylist.get(i+1);
switch (a.compareTo(b))
{
case 1://the objects in array x are in the right order
break;
case -1:// objects out of order, they must be changed.
    xsave=mylist.get(i);
    ysave=mylist.get(i+1);
    mylist.remove(i);
    mylist.add(i,ysave);
    mylist.remove(i+1);
    mylist.add(i+1,xsave);
    // mylist.add(i,mylist.get(i+1));
    //mylist.add(i+1,xsave);
    isw=1;
    break;
    default://objects are equal no chanbe
}//end of switch

    }//end of for
    }//end of while
}// ManageandSort
public void removem(int i)
{//This removes the i'th value from the list
if((i>=0)&&(i<=mcount-1))
{
    mylist.remove(i);
    mcount--;
}
return;
}//end of removem
} // end of GenericManager
