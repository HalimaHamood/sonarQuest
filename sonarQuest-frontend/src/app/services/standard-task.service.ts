import {World} from './../Interfaces/World';
import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';


import {Observable, ReplaySubject} from 'rxjs';
import {StandardTask} from '../Interfaces/StandardTask';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import {Task} from '../Interfaces/Task';

@Injectable()
export class StandardTaskService {

  private standardTaskSubject;


  constructor(public http: HttpClient) {
    this.standardTaskSubject = new ReplaySubject(1);
  }

  getStandardTasksForWorld(world: World): Observable<StandardTask[]> {
    this.http.get<StandardTask[]>(`${environment.endpoint}/task/standard/world/${world.id}`).subscribe(
      result => this.standardTaskSubject.next(result),
      err => this.standardTaskSubject.error(err)
    );
    return this.standardTaskSubject.asObservable();
  }

  updateStandardTask(task: StandardTask): Promise<StandardTask> {
    return this.http.put<StandardTask>(`${environment.endpoint}/task/standard`, task)
      .toPromise()
      .catch(this.handleError);
  }

  updateStandardTasksForWorld(world: World): Promise<Task[]> {
    return this.http.get<Task[]>(`${environment.endpoint}/task/updateStandardTasks/${world.id}`)
      .toPromise()
      .catch(this.handleError);
  }

  public getFreeStandardTasksForWorldExcept(world: World): Promise<StandardTask[]> {
    const params = new HttpParams().set('id', String(world.id));
    return this.http.get<StandardTask[]>(`${environment.endpoint}/task/standard/free/world`, { params: params })
      .toPromise()
      .catch(this.handleError);
  }

  private handleError(error: HttpErrorResponse | any) {
    let errMsg: string;
    if (error instanceof HttpErrorResponse) {
      errMsg = `${error.status} - ${error.statusText || ''}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Promise.reject(errMsg);
  }
}
