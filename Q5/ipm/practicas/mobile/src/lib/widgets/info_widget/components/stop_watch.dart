import 'dart:async';
import 'package:flutter/cupertino.dart';

class StopWatch extends StatefulWidget {
  StopWatch({Key? key,required this.startTime}) : super(key: key);

  DateTime startTime;

  @override
  State<StopWatch> createState() => _StopWatchState();
}

class _StopWatchState extends State<StopWatch> {
  String _timetodisplay = "00:00:00";
  final _stopWatch = Stopwatch();
  final _timeOut = const Duration(seconds: 1);

  void _startChronometer() {
    _stopWatch.start();
    Timer(_timeOut,_keepRuning);
  }

  void _keepRuning() {
    setState(() {
      DateTime now = DateTime.now();
      _timetodisplay = now.difference(widget.startTime).inHours.toString().padLeft(2,"0") + ":"
        + (_stopWatch.elapsed.inMinutes%60).toString().padLeft(2,"0") + ":"
        + (_stopWatch.elapsed.inSeconds%60).toString().padLeft(2,"0");
    });
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    _startChronometer();

    return Text(
      _timetodisplay,
      textAlign: TextAlign.center,
      style: TextStyle(
        color: const Color(0xFF553C8B),
        fontWeight: FontWeight.bold,
        fontSize: size.height * 0.03,
      ),
    );
  }
}
