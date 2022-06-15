import 'package:flutter/material.dart';
import 'package:flutter_datetime_picker/flutter_datetime_picker.dart';

class RoundedDateSelector extends StatefulWidget {
  final String placeholder;
  final TextEditingController controller;

  const RoundedDateSelector({
    Key? key,
    required this.placeholder,
    required this.controller,
  }) : super(key : key);

  @override
  State<RoundedDateSelector> createState() => _RoundedDateSelectorState();
}

class _RoundedDateSelectorState extends State<RoundedDateSelector> {
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return TextButton(
      onPressed: () {
        DatePicker.showDateTimePicker(context, showTitleActions: true,
          minTime: DateTime(1970, 1, 1), maxTime: DateTime.now(), 
          onConfirm: (date) {
            widget.controller.text = date.toString().substring(0, date.toString().length-6-1);
            setState((){});
          },
          currentTime: DateTime.now(),
          locale: Localizations.localeOf(context).languageCode=='es'?LocaleType.es:LocaleType.en
        );
      },
      child: Text(
        ( widget.controller.text == "" ) ? widget.placeholder : widget.controller.text,
        textAlign: TextAlign.center,
        style: ( widget.controller.text == "" ) ?
          TextStyle(fontSize: size.height*0.02, color: Colors.grey[400], fontWeight: FontWeight.w400)
          :
          TextStyle(fontSize: size.height*0.02 , color: Colors.grey[600], fontWeight: FontWeight.w400)
      ),
    );
  }
}
