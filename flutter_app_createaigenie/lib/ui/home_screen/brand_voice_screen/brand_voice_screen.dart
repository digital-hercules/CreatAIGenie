import 'package:create_ai_genie/ui/common_widgets/round_button.dart';
import 'package:create_ai_genie/utils/extensions/text_style_extension.dart';
import 'package:flutter/material.dart';

import 'dart:io';

import '../../../utils/constants/app_spacer_constants.dart';
import '../../../utils/constants/scale_factor.dart';
import '../home.dart';



class BrandVoiceScreen extends StatefulWidget {
  const BrandVoiceScreen({super.key});

  @override
  State<BrandVoiceScreen> createState() => _BrandVoiceScreenState();
}

class _BrandVoiceScreenState extends State<BrandVoiceScreen> {
  bool showProgress = false;
  bool visible = false;

  final _formkey = GlobalKey<FormState>();

  final TextEditingController passwordController = TextEditingController();
  final TextEditingController confirmpassController =
  TextEditingController();
  final TextEditingController name = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController mobileController = TextEditingController();
  final TextEditingController hobbiesController = TextEditingController();
  final TextEditingController interestsController = TextEditingController();
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
                          'Brand Voice',
                          style: context.displayMedium,
                          textScaler: TextScaler.linear(
                            ScaleSize.textScaleFactor(context),
                          ),
                          maxLines: 1,
                        ),
                        AppSpacer.p10(),
                        Text(
                          'Tell us about your company',
                          style: context.displayLarge,
                        ),
                        SizedBox(
                            height: 70,
                            child: Image.asset(
                                'assets/icons/brand_icon.png',height: 100,fit:BoxFit.fitHeight,)),
                        AppSpacer.p10(),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            Text('Use Personalised Profile ',style: context.displaySmall,),
                            AppSpacer.p8(),
                            Image.asset('assets/icons/s_switch_icon.png')

                          ],
                        ),
                        AppSpacer.p10(),

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
                                    'Brand Name',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: name,
                                    decoration: InputDecoration(
                                      hintText: 'Enter your brand name',
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
                                    'Objective',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: emailController,
                                    decoration: const InputDecoration(
                                      hintText: 'Enter objectives of the brand',
                                      enabled: true,
                                    ),
                                    validator: (value) {
                                      if (value!.isEmpty) {
                                        return "objective cannot be empty";
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
                                    'Brand Theme',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: mobileController,
                                    decoration: const InputDecoration(
                                      hintText: 'Enter theme of brand',
                                    ),
                                    validator: (value) {
                                      if (value!.isEmpty) {
                                        return "Number cannot be empty";
                                      }
                                      if (!RegExp(r'^[0-9]+$')
                                          .hasMatch(value)) {
                                        return ("Please enter a valid number only numeric");
                                      } else {
                                        return null;
                                      }
                                    },
                                    onChanged: (value) {},
                                    keyboardType: TextInputType.number,
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
                                    'Other details you want to add',
                                    style: context.displaySmall!
                                        .copyWith(color: const Color(0xff666666)),
                                  ),
                                  TextFormField(
                                    controller: hobbiesController,
                                    decoration: InputDecoration(
                                      hintText: 'Express more about the brand',
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

                                    onChanged: (value) {},
                                    keyboardType: TextInputType.emailAddress,
                                  ),
                                ],
                              ),

                              const SizedBox(
                                height: 30,
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
                            text: 'Save',
                            voidCallBack: () {
                              if (_formkey.currentState!.validate()) {
                                Navigator.push(context, MaterialPageRoute(builder: (context) => const Home(),));

                              }
                            }),
                        AppSpacer.p10(),

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
