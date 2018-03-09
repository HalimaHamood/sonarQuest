export interface Quest{
  id : number,
  title :  string ,
  story :  string ,
  status :  string ,
  gold : number,
  xp : number,
  world : {
     id : number,
     name :  string ,
     project :  string ,
     active : true
  },
  adventure : {
     id : number,
     title :  string ,
     story :  string,
     status :  string ,
     gold : string,
     xp : string
  },
  tasks: Task[];
   participations : Participation[]
}

export interface Task{
  id : number,
  title :  string ,
  status :  string ,
  gold : number,
  xp : number,
  key : string,
  component :  string ,
  severity :  string ,
  type :  string ,
  debt : number,
  message :  string
}

export interface Participation{
   id : number,
   tasks: Task[],
   developer : {
     id : number,
     username :  string ,
     gold : number,
     xp : number
  }

}
