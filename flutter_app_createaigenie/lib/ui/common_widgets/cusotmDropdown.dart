
import 'package:flutter/material.dart';




class CustomDropDownButton extends StatefulWidget {

  List<String> list;
  ValueChanged<String> onPressed;
  Color color;
  String hint;
  String selected;

  CustomDropDownButton({super.key, required this.list, required this.onPressed, required this.color,  this.hint='',this.selected=''});

  @override
  State<CustomDropDownButton> createState() => _CustomDropDownButtonState();
}

class _CustomDropDownButtonState extends State<CustomDropDownButton> {
  String? selectedValue;

  @override
  void initState() {
    super.initState();
    selectedValue =  null;
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text('Output In'),
        const SizedBox(height: 5,),
        SizedBox(
          height: 55,
          width: 150,
          child: InputDecorator(
              decoration: const InputDecoration(
                  border: OutlineInputBorder(
                    gapPadding: 0,
                  )),
              child: DropdownButton(
                hint: Text(widget.hint),
                isDense: true,
                style: const TextStyle(fontSize: 15,fontWeight: FontWeight.bold),
                padding: EdgeInsets.zero,
                itemHeight: 50,
                isExpanded: true,
                underline: Container(),
                value: selectedValue,
                icon: const Icon(Icons.keyboard_arrow_down),
                items: widget.list.map((String items) {
                  return DropdownMenuItem(

                    value: items,
                    child: Text(items),
                  );
                }).toList(),
                onChanged:  (value) {
                  setState(() {
                    selectedValue = value;
                  });
                  widget.onPressed(value!);
                },
              )),
        ),
      ],
    );


















  }
}
