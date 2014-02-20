import akka.actor._
import scala.util.Random
import scala.math.sqrt
//topology is one of full, 2D, line, imp2D, algorithm is one of gossip, push-sum.
case class Init(i0:Int,neighbors0:List[Int],allnodes0:List[ActorRef],listener0:ActorRef)
case class Gossip
case class Pushsum(s0:Double,w0:Double)
case class id(i:Int)
case class Initlistener(allnodes:List[ActorRef],system:ActorSystem)
case class Pushsumdone

object Project2 {
  def main(args : Array[String]) = {
    if(args.length != 3){
      println("Input error!"+'\n'+"Project2 numNodes topology algorithm")
    }else {
      val full = "full"
      val twoD = "2D"
      val line = "line"
      val imp2D = "imp2D"
      val gossip = "gossip"
      val pushsum = "push-sum"
      
      var Numofnodes = args(0).toInt
      val topology = args(1).toString()
      val algorithm =args(2).toString()
       /*var Numofnodes=500
       val topology="imp2D"
       val algorithm="gossip"*/
        
 /*for (N<-50 to 2000 if N%50==0){     
      Numofnodes=N*/
      val system = ActorSystem("GossipSystem")
      var allnodes: List[ActorRef] = Nil
      
      if(topology==twoD || topology==imp2D) Numofnodes = ChangetoPerfectnum(Numofnodes)
      val numroot = math.sqrt(Numofnodes.toDouble)
      
      var listener = system.actorOf(Props[Listener])
      
 //     println(Numofnodes+ "  " + topology + "  " + algorithm)
      
      var i=0
      while(i<Numofnodes){
        allnodes ::= system.actorOf(Props[Nodes])
        i = i+1
      }
      // Init allnodes in terms of topology
      if(full.equalsIgnoreCase(topology)){
        var i =0 
        while(i<allnodes.length){
          var neighbors:List[Int] = Nil
          var j =0 
          while(j<allnodes.length){
            if(j != i)
              neighbors ::= j
            j = j + 1
          }
          
          allnodes(i) ! Init(i,neighbors,allnodes,listener)
          i = i + 1
          
        }
        
      }
      if(twoD.equalsIgnoreCase(topology)){
        var i:Int =0
        while(i<allnodes.length){
          var neighbors:List[Int] = Nil
          if(!isOnTop(i,allnodes.length)) neighbors ::= (i-numroot.toInt)
          if(!isOnBottom(i,allnodes.length)) neighbors ::= (i+numroot.toInt)
          if(!isOnLeft(i,allnodes.length)) neighbors ::= (i-1)
          if(!isOnRight(i,allnodes.length)) neighbors ::= (i+1)
          
          allnodes(i) ! Init(i,neighbors,allnodes,listener)
          i = i + 1
        }
        
      }
      if(line.equalsIgnoreCase(topology)){
        var i:Int =0
        while(i<allnodes.length){
          var neighbors:List[Int] = Nil
          if(i!=0) neighbors ::= (i-1)
          if(i<allnodes.length-1) neighbors ::= (i+1)
          
          allnodes(i) ! Init(i,neighbors,allnodes,listener)
          i = i + 1
        }
        
      }
      if(imp2D.equalsIgnoreCase(topology)){
        var i:Int =0
        while(i<allnodes.length){
          var neighbors:List[Int] = Nil
          if(!isOnTop(i,allnodes.length)) neighbors ::= (i-numroot.toInt)
          if(!isOnBottom(i,allnodes.length)) neighbors ::= (i+numroot.toInt)
          if(!isOnLeft(i,allnodes.length)) neighbors ::= (i-1)
          if(!isOnRight(i,allnodes.length)) neighbors ::= (i+1)
         // var flag = false
          var j = 0
          do{
            j = Random.nextInt(allnodes.length)
            for(x<-neighbors){
                if(j == x) j = -1 
            }
            if(j == i) j = -1   
          }while(j == -1)
 
          neighbors ::= j
  
          allnodes(i) ! Init(i,neighbors,allnodes,listener)
          i = i + 1
        }
        
      }
      //Init Listener
      listener ! Initlistener(allnodes,system)
      //Init in terms of algorithm
      if(algorithm.equalsIgnoreCase(gossip)){
        allnodes(0) ! Gossip
      }else{
        allnodes(0) ! Pushsum(0,1)
      }
 //}   
    }
    
    
    
    def ChangetoPerfectnum(x:Int):Int={
      var ai = x
      while(math.sqrt(ai.toDouble)%1 !=0){
        ai=ai+1
      }
      ai     
    }
    def isOnTop(a:Int,b:Int):Boolean={
      if(a.toDouble < math.sqrt(b.toDouble)) true 
      else false
    }
    def isOnBottom(a:Int,b:Int):Boolean={
      if(a.toDouble >= b.toDouble-math.sqrt(b.toDouble)) true 
      else false
    }
    def isOnLeft(a:Int,b:Int):Boolean={
    if( a.toDouble % math.sqrt(b.toDouble) == 0) true 
      else false
    }
    def isOnRight(a:Int,b:Int):Boolean={
      if( (a.toDouble+1)%math.sqrt(b.toDouble) == 0) true 
      else false
    }
    
    
  //}
  	}
  //}
}
  
 class Nodes extends Actor{
   val rumorTerminationgossip = 10
   val rumorTerminationPush = math.pow(10, -10)
   var gossipcount:Int = 0
   var pushsumcount:Int = 1 
   var flag = false 
   
   var i:Int = 0
   var neighbors:List[Int] = Nil
   var allnodes:List[ActorRef] = Nil 
   var listener:ActorRef = null
   
   var s:Double = 0
   var w:Double = 0.0
   val div =2
   
   
   def receive ={
     case Init(i0,neighbors0,allnodes0,listener0) => {
       i = i0
       s = i0
       neighbors = neighbors0
       allnodes = allnodes0
       listener = listener0
     //         println("i+neighbors  :" + i +'\t'+neighbors)
     }
     case Gossip => {

       if(gossipcount < rumorTerminationgossip){
         var randomnode = 0
         gossipcount = gossipcount + 1
        // println(i+"  "+"gossipcount" + gossipcount )
         listener ! id(i)
         for(c <- 0 to 2 ){
          randomnode = Random.nextInt(neighbors.length)
//          println("randomnode :"+ randomnode)
          allnodes(neighbors(randomnode)) ! Gossip
         }
       }

     }
     case Pushsum(s0,w0) => {
       var oldsum = s/w
       s += s0
       w += w0
       s = s/div
       w = w/div
       var newsum = s/w
    //   println("newsum   "+i+"  "+newsum + "   " + s + "  "+ w)
       var randomnode = 0
       if (math.abs(newsum - oldsum) > rumorTerminationPush){
         flag = true
         randomnode = Random.nextInt(neighbors.length)
         allnodes(neighbors(randomnode)) ! Pushsum(s,w)
       }else{
         if(!flag) pushsumcount =pushsumcount + 1
         flag = false
         if(pushsumcount > 2){
           listener ! Pushsumdone
         }else{
           randomnode = Random.nextInt(neighbors.length)
           allnodes(neighbors(randomnode)) ! Pushsum(s,w)
         }
         
       }
     
     }
   }
   override def postStop(){
   // 	println("Post Terimation --- For Actor  "+i+"\tallnodes: "+allnodes.length +" \ts/w: "+(s/w));
    }
 }
 class Listener extends Actor{
   var result:List[Int] = Nil
   val b = System.currentTimeMillis
   var allnodes:List[ActorRef] = Nil
   var system:ActorSystem = null
   //println(b)
   def receive ={
     case Initlistener(allnodes0,sys) =>{
       allnodes = allnodes0
       system = sys
     }
     case id(i) =>{
       var flag = false
      // println("number :"+i)
       if(result.length == 0) result ::=i
       for(c<-0 until result.length){
         if( result(c) == i) flag = true 
       }
       if(!flag) result ::= i
  //     println("result:   "+result+"\tlenghth:   "+result.length)
       if(result.length.toDouble/allnodes.length.toDouble>0.9){
         system.shutdown
       }
     }
     case Pushsumdone =>{
       system.shutdown
     }
   }
   override def postStop(){
     println("numberofnodes:  "+allnodes.length+"\tRuntime: "+(System.currentTimeMillis-b))
   }
 }
