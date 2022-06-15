import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';

class LocationNotFoundException implements Exception {
  String errMsg()=> "Especified location does not exist";
}
class EventModel extends ChangeNotifier{
  String? _eventName;
  String? _eventLocation;
  int? _locationId;
  DateTime? _startTime;

  bool _started = false;
  //Hay que checkear null-safety al utilizar los strings y el datetime

  //Para que no se puedan modificar con setters las variables
  String get eventName => _eventName??"NoName";
  String get eventLocation => _eventLocation??"NoLocation";
  int get locationId => _locationId??0;
  DateTime? get startTime => _startTime;
  bool get started => _started;

  Future<bool> _checkLocation(String location)async {
    String url = "http://ipm.hermo.me/api/rest/facilities";
    Map<String,String> auth={
      "x-hasura-admin-secret":"myadminsecretkey"
    };
    final response = await http.get(Uri.parse(url),headers:auth);
    if (response.statusCode != 200) {
      throw InternalErrorException();
    }
    final responseJson = jsonDecode(response.body);
    final facilities = responseJson["facilities"];
    try{
      final matchedFacility = facilities.firstWhere((facility) => 
        facility["name"].toLowerCase()== location.toLowerCase());
      _locationId = matchedFacility["id"];
      _eventLocation = matchedFacility["name"];
    }on StateError catch (_){
      return false;
    }
    return true;
  }
  Future<void> startEvent(String eventName, String eventLocation,DateTime? startTime)async {
    if(!(await _checkLocation(eventLocation))){
      throw LocationNotFoundException();
    }

    _eventName = eventName;
    //eventLocation se asigna en checkLocation, para que se ponga igual
    //que está guardado en la base de datos
    _startTime = startTime??DateTime.now();
    _started = true;
    notifyListeners();
  }
  void stopEvent(){
    _eventName = null;
    _eventLocation = null;
    _startTime = null;
    _started = false;
    notifyListeners();
  }
}

class NoIdentificationEspecifiedException implements Exception {
  String errMsg() => "No UUID or phone especificated";
}
class NoMatchedUserException implements Exception {
  String errMsg() => "No user matched with the especified fields";
}
class Access{

  Access({
    required this.name,
    required this.surname,
    this.uuid,
    
    //Esto es para poder hacer que el default sea now
    this.inTime,
    this.inTemperature,
    this.outTime,
    this.outTemperature,
    this.phone,
    });

  final String name;
  final String surname;
  String? uuid;
  //Si no se especifica
  DateTime? inTime;
  double? inTemperature;

  //Se puede modificar el outTime una vez la clase ya esté creada
  DateTime? outTime;
  double? outTemperature;

  //No usar campos de abajo hasta que esté bien hecho
  String? phone;

  Future<void> correctUser()async {
    if (uuid ==null || uuid == ""){
      if(phone !=null && phone !=""){

        String url = "http://ipm.hermo.me/api/rest/user?name=$name&surname=$surname";
        Map<String,String> auth={
          "x-hasura-admin-secret":"myadminsecretkey"
        };
        final response = await http.get(Uri.parse(url),headers:auth);
        if (response.statusCode != 200) {
          throw InternalErrorException();
        }
        final responseJson = jsonDecode(response.body);
        final users = responseJson["users"];
        try{
        final matchUser = users.firstWhere((user) =>user["phone"] == phone);
        uuid = matchUser["uuid"];
        }on StateError catch (_){
          throw NoMatchedUserException();
        }
        
      }else{
        throw NoIdentificationEspecifiedException();
      }
    }
  }


  //mirar como hacer bien el equals
  @override
  bool operator ==(o) => o is Access && name == o.name && surname == o.surname
   && uuid == o.uuid;
  
  @override
  int get hashCode => hashValues(name.hashCode,surname.hashCode,uuid.hashCode);
  //definir un método equals para poder buscar bien en la lista.
}
class AlreadyInsideException implements Exception {
  String errMsg() => "Person already inside";
}

class NotInsideException implements Exception {
  String errMsg() => "Person is not on inside list";
}

class TemperatureNotEspecifiedException implements Exception {
  String errMsg() => "Temperature not especified ";
}
class InternalErrorException implements Exception {
  String errMsg() => "API could not handle the request";
}

class AccessModel extends ChangeNotifier{
  //Permitir que sea null si no se ha hecho aún el init
  int? _locationId;
  DateTime? _startedDate;
  List<Access> _inside = [];
  List<Access> _totalAccess=[];

  int get locationId => _locationId??0;
  UnmodifiableListView<Access> get inside => UnmodifiableListView(_inside);
  UnmodifiableListView<Access> get totalAccess => UnmodifiableListView(_totalAccess);
  //Las listas son inmutables, pero los objetos de dentro no. Hay que tener
  //cuidado al modificarlos

  void initEvent(int locationId,DateTime startedDate){
    _locationId = locationId;
    _startedDate = startedDate;
  }

  Future<bool> _checkUser(Access access) async {
    //Petición a la BD por si existe la persona
    String url = "http://ipm.hermo.me/api/rest/users/${access.uuid}";
    Map<String,String> auth={
      "x-hasura-admin-secret":"myadminsecretkey"
    };
    final response = await http.get(
      Uri.parse(url),
      headers:auth,
    );
    if (response.statusCode != 200) {
      throw InternalErrorException();
    }
    final responseJson = jsonDecode(response.body);
    
    final users = responseJson["users"];
    bool result = users.length>0 && users[0]["name"] == access.name &&
      users[0]["surname"] == access.surname;

    return result;
  }

  
  //Se lanza una UserNotFoundException si se intenta crear un acceso de un usuario
  //que no existe

  Future<void> postAccess(String uuid, int facilityId, DateTime timestamp,
   double temperature, String type) async {
    String url = "http://ipm.hermo.me/api/rest/access_log";
    Map<String,String> auth={
      "x-hasura-admin-secret":"myadminsecretkey"
    };
    final response = await http.post(
      Uri.parse(url),
      headers:auth,
      body: jsonEncode(<String, dynamic>{
      'user_id': uuid,
      'facility_id': facilityId,
      'timestamp':timestamp.toIso8601String(),
      'type':type,
      'temperature':temperature.toString(),
      })
    );
    
    if (response.statusCode != 200) {
      throw InternalErrorException();
    }
  }
  Future<void> updateAccess() async {
    String url = "http://ipm.hermo.me/api/rest/facility_access_log/${locationId}/daterange";
    final body ={"startdate":_startedDate!.toIso8601String(),"enddate":DateTime.now().toIso8601String()};
    Map<String,String> auth={
      "x-hasura-admin-secret":"myadminsecretkey",
    };
    http.Request request = http.Request("GET",Uri.parse(url));
    request.body=json.encode(body);
    request.headers.addAll(auth);
    final response = await request.send();

    if (response.statusCode != 200) {
      throw InternalErrorException();
    }
    
    String dataString =await response.stream.bytesToString();
    final data = json.decode(dataString);
    List<dynamic>access = data["access_log"];
    
    List<Access> fullAccess=[];
    List<Access> inAccess=[];

    List<dynamic> inAcc = access.where((element) => element["type"]=="IN").toList();
    inAcc.sort(
      (a,b) => DateTime.parse(a["timestamp"]).compareTo(DateTime.parse(b["timestamp"])) 
    );

    List<dynamic> outAcc = access.where((element) => element["type"]=="OUT").toList();
    outAcc.sort(
      (a,b) => DateTime.parse(a["timestamp"]).compareTo(DateTime.parse(b["timestamp"])) 
    );

    inAcc.forEach((inAcc) {
      final user = inAcc["user"];

      try{
        final matchedAccess = outAcc.firstWhere((outAcc) => outAcc["user"]["uuid"]== inAcc["user"]["uuid"]);
        outAcc.remove(matchedAccess);
        fullAccess.add(Access(name:user["name"],surname:user["surname"],uuid:user["uuid"],
        inTime:DateTime.parse(inAcc["timestamp"]),inTemperature:double.parse(inAcc["temperature"]),
        outTime:DateTime.parse(matchedAccess["timestamp"]),outTemperature:double.parse(matchedAccess["temperature"])
        ));
      }on StateError catch (_){
        Access access = Access(name:user["name"],surname:user["surname"],uuid:user["uuid"],
        inTime:DateTime.parse(inAcc["timestamp"]),inTemperature:double.parse(inAcc["temperature"]));
        fullAccess.add(access);
        inAccess.add(access);      
      }
    });

    _inside = inAccess;
    _totalAccess = fullAccess;
    notifyListeners();
    
  }
  Future<void> registerAccess(String type,Access access)async {

    if (!(await _checkUser(access))){
      throw NoMatchedUserException();
    }

    switch(type){
      case "IN":
        //Actualizar antes, para ver si ya está dentro
        await updateAccess();

        if(access.inTemperature==null){
          throw TemperatureNotEspecifiedException();
        }
        if(_inside.contains(access)){
          throw AlreadyInsideException();
        }

        //Si el acceso no tiene espeficiado el inTime, se lo ponemos a la fecha
        //actual, mejor hacer la comprobación aquí que hacerlo default en la clase.

        access.inTime = access.inTime??DateTime.now();
        //Asegurarme de que se añade con tiempo de salida null
        access.outTime = null;

        // Se hacen las dos peticiones a la BD juntas por consistencia
        await postAccess(access.uuid!,_locationId!, access.inTime!,
          access.inTemperature!,"IN");

        //Para evitar hacer la petición de actualizar a la BD
        _inside.add(access);
        _totalAccess.add(access);
        notifyListeners();

        //Actualizar al introducir el acceso, para tener la lista consistente
        //updateAccess();
        break;

      case "OUT":

        //Actualizar antes, para ver si ya está dentro
        await updateAccess();

        if(access.outTemperature==null){
          throw TemperatureNotEspecifiedException();
        }

        if(!_inside.contains(access)){
          throw NotInsideException();
        }

        //Asegurarse de que los campos no sean null
        access.outTime = access.outTime??DateTime.now();

        
        //Para evitar hacer la petición a l BD
        Access insideListAccess = _inside.firstWhere((element) => element == access);
        insideListAccess.outTime = access.outTime;
        insideListAccess.outTemperature = access.outTemperature;

        

        await postAccess(access.uuid!,_locationId!, access.outTime!,
          access.outTemperature!,"OUT");

        //Para evitar hacer la petición de actualizar a la BD

        _inside.remove(access);
        notifyListeners();

        //Actualizar después, para tener la lista consistente
        //updateAccess();

        break;

      default:
        break;
    }
  }
}
