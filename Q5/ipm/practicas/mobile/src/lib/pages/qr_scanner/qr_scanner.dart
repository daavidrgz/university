import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:qr_code_scanner/qr_code_scanner.dart';
import 'package:vibration/vibration.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Scanner extends StatefulWidget {
  final TextEditingController name;
  final TextEditingController surname;
  final TextEditingController uuid;

  const Scanner({
    Key? key,
    required this.name,
    required this.surname,
    required this.uuid
  }) : super(key: key);

  @override
  _ScannerState createState() => _ScannerState();
}

class _ScannerState extends State<Scanner> {
  bool _flashOn = false;
  final GlobalKey _qrKey = GlobalKey();
  late QRViewController _controller;
  Color borderColor = Colors.white;

  void changeBorderColor(Color color) {
    setState(() {
      borderColor = color;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: <Widget>[
          QRView(
            key: _qrKey,
            overlay: QrScannerOverlayShape(
              borderColor: borderColor,
              borderRadius: 25,
              borderWidth: 5,
            ),
            onQRViewCreated: (QRViewController controller) {
              _controller = controller;
              _controller.scannedDataStream.listen((data) {
                var values = data.code.split(",");
                if ( values.length == 3 ) {
                  changeBorderColor(Colors.green);
                  _controller.stopCamera();
                  widget.name.text = values [0];
                  widget.surname.text = values [1];
                  widget.uuid.text = values [2];
                  Vibration.vibrate();
                  Navigator.of(context).maybePop(values);
                } else {
                  changeBorderColor(Colors.red);
                }
              });
            },
          ),
          Align(
            alignment: Alignment.topCenter,
            child: Container(
              margin: const EdgeInsets.only(top: 60),
              child: Text(
                AppLocalizations.of(context)!.scanner,
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  color: Colors.white
                ),
              ),
            ),
          ),
          Align(
            alignment: Alignment.bottomCenter,
            child: ButtonBar(
              alignment: MainAxisAlignment.spaceAround,
              children: <Widget>[
                IconButton(
                  color: Colors.white,
                  icon: Icon(_flashOn ? Icons.flash_on : Icons.flash_off),
                  onPressed: () {
                    setState(() {
                      _flashOn = !_flashOn;
                    });
                    _controller.toggleFlash();
                  },
                ),
                IconButton(
                  color: Colors.white,
                  icon: const Icon(Icons.close),
                  onPressed: () => Navigator.of(context).maybePop(),
                )
              ]
            ),
          ),
        ],
      ),
    );
  }
}
