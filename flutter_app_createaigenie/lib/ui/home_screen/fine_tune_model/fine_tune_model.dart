import 'package:create_ai_genie/ui/common_widgets/round_button.dart';
import 'package:create_ai_genie/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';

import 'dart:io';

import '../../../utils/constants/app_spacer_constants.dart';
import '../../../utils/constants/scale_factor.dart';
import '../home.dart';



class FineTuneModel extends StatefulWidget {
  const FineTuneModel({super.key});

  @override
  State<FineTuneModel> createState() => _FineTuneModelState();
}

class _FineTuneModelState extends State<FineTuneModel> {
  bool showProgress = false;
  bool visible = false;

  final _formkey = GlobalKey<FormState>();

  final TextEditingController passwordController = TextEditingController();
  final TextEditingController confirmpassController =
  TextEditingController();
  final TextEditingController nameController = TextEditingController();
  final TextEditingController companyNameController = TextEditingController();
  final TextEditingController roleController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController mobileController = TextEditingController();
  final TextEditingController bioController = TextEditingController();
  final TextEditingController weight = TextEditingController();
  final bool _isObscure = true;
  final bool _isObscure2 = true;
  File? file;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: GestureDetector(
          onTap: (){Navigator.pop(context);},
          child: const Icon(
            Icons.arrow_back_ios_rounded,
            color: Color(0xffB219F0),
          ),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.only(
          left: 20,
          right: 20,
        ),
        child: CustomScrollView(
          slivers: [
            SliverFillRemaining(
                hasScrollBody: false,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        const SizedBox(
                          width: double.infinity,
                        ),
                        Text(
                          'Fine Tune Model',
                          style: context.displayMedium,
                          textScaler: TextScaler.linear(
                            ScaleSize.textScaleFactor(context),
                          ),
                          maxLines: 1,
                        ),
                        AppSpacer.p10(),
                        Text(
                          'Enter Bot Details',
                          style: context.displayLarge,
                        ),
                        Stack(
                          children: [
                            SizedBox(
                                height: 90,
                                child: Image.asset(
                                    'assets/icons/bot_icon.png')),
                            Positioned(
                                right: 10,
                                top: 15,
                                child:
                                Image.asset('assets/icons/sign_add.png',height: 17,)),
                          ],
                        ),
                        Form(
                          key: _formkey,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'BotName',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: nameController,
                                    decoration: InputDecoration(
                                      hintText: 'Enter the bot name',
                                      hintStyle:
                                      const TextStyle(color: Color(0xffBABABA)),
                                      enabled: true,
                                      contentPadding: const EdgeInsets.only(
                                          left: 14.0, bottom: 8.0, top: 8.0),
                                      focusedBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                      enabledBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                    ),
                                    validator: (value) {
                                      if (value!.isEmpty) {
                                        return "Name cannot be empty";
                                      }
                                      if (!RegExp(r'^[a-zA-Z]+$')
                                          .hasMatch(value)) {
                                        return ("Please enter a valid Name only Alphabets");
                                      } else {
                                        return null;
                                      }
                                    },
                                    onChanged: (value) {},
                                    keyboardType: TextInputType.emailAddress,
                                  ),
                                ],
                              ),
                              const SizedBox(
                                height: 20,
                              ),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'Bot Category',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: companyNameController,
                                    decoration: InputDecoration(
                                      hintText: 'Select Category for your bot',
                                      hintStyle:
                                      const TextStyle(color: Color(0xffBABABA)),
                                      enabled: true,
                                      contentPadding: const EdgeInsets.only(
                                          left: 14.0, bottom: 8.0, top: 8.0),
                                      focusedBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                      enabledBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                    ),
                                    validator: (value) {
                                      if (value!.isEmpty) {
                                        return "Name cannot be empty";
                                      }
                                      if (!RegExp(r'^[a-zA-Z]+$')
                                          .hasMatch(value)) {
                                        return ("Please enter a valid Name only Alphabets");
                                      } else {
                                        return null;
                                      }
                                    },
                                    onChanged: (value) {},
                                    keyboardType: TextInputType.emailAddress,
                                  ),
                                ],
                              ),
                              const SizedBox(
                                height: 20,
                              ),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'Bot Model',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: roleController,
                                    decoration: InputDecoration(
                                      hintText: 'Choose model for bot',
                                      hintStyle:
                                      const TextStyle(color: Color(0xffBABABA)),
                                      enabled: true,
                                      contentPadding: const EdgeInsets.only(
                                          left: 14.0, bottom: 8.0, top: 8.0),
                                      focusedBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                      enabledBorder: OutlineInputBorder(
                                        borderSide: const BorderSide(
                                            color: Color(0xffCBD2E0)),
                                        borderRadius:
                                        BorderRadius.circular(8),
                                      ),
                                    ),
                                    validator: (value) {
                                      if (value!.isEmpty) {
                                        return "Role cannot be empty";
                                      }
                                      else {
                                        return null;
                                      }
                                    },
                                    onChanged: (value) {},
                                    keyboardType: TextInputType.emailAddress,
                                  ),
                                ],
                              ),
                              const SizedBox(
                                height: 20,
                              ),

                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'Bot Instructions',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(

                                    controller: passwordController,
                                    decoration: const InputDecoration(

                                      hintText: 'Enter instructions for your bot',
                                    ),
                                    validator: (value) {
                                      RegExp regex = RegExp(r'^.{6,}$');
                                      if (value!.isEmpty) {
                                        return "Password cannot be empty";
                                      }
                                      if (!regex.hasMatch(value)) {
                                        return ("please enter valid password min. 6 character");
                                      } else {
                                        return null;
                                      }
                                    },
                                    onChanged: (value) {},
                                  ),
                                ],
                              ),

                              const SizedBox(
                                height: 20,
                              ),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                Text('Upload a File?',style: context.displaySmall!.copyWith(color: Colors.black),),
                                AppSpacer.p8(),
                                const Icon(Icons.upload_file),
                              ],),
                              const SizedBox(
                                height: 20,
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                    const Spacer(),
                    Column(
                      children: [
                        RoundButton(
                            text: 'Create',
                            voidCallBack: () {
                              if (_formkey.currentState!.validate()) {
                                Navigator.push(context, MaterialPageRoute(builder: (context) => const Home(),));

                              }
                            }),

                        const SizedBox(
                          height: 20,
                        )
                      ],
                    )
                  ],
                ))
          ],
        ),
      ),
    );
  }
}
