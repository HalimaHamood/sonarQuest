

export interface Level {
  id: number,
  name: string ,
  min: number,
  max: number
}
export interface AvatarClass{
  id: number,
  name: string
}

export interface AvatarRace{
  id: number,
  name: string
}

export interface Artefact {
  id: number,
  name: string  ,
  icon: string  ,
  price: number,
  minLevel: Level
}


export interface Developer {
  id: number,
  username: string ,
  gold: number,
  xp: number,
  level:  Level,
  picture: string,
  aboutMe: string,
  avatarClass: AvatarClass,
  avatarRace: AvatarRace,
  artefacts: Artefact[]
}
