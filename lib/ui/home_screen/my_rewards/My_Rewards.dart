import 'package:flutter/material.dart';





class MyRewards extends StatelessWidget {
  const MyRewards({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Rewards UI',
      theme: ThemeData(
        primarySwatch: Colors.purple,
      ),
      home: const MyRewardsPage(),
    );
  }
}

class MyRewardsPage extends StatelessWidget {
  const MyRewardsPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        leading: const Icon(Icons.arrow_back, color: Colors.purple),
        centerTitle: true,
        title: const Text('My Rewards', style: TextStyle(color: Colors.black)),
      ),
      body: SingleChildScrollView(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            const SizedBox(height: 20),
            Center(
              child: Image.network(
                'assets/icons/Reward_icon.png',
                height: 139,
                width:194,
              ),
            ),
            const SizedBox(height: 20),
            Card(
              color: Colors.purple[100],
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(15.0),
              ),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  children: [
                    const Text(
                      'MY POINTS',
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                        color: Colors.white,
                      ),
                    ),
             const SizedBox(height: 20),
                    // Highlighted section starts here
                    Stack(
                      alignment: Alignment.center,
                      children: [
                        Image.asset(
                          'assets/icons/Reward2_icon.png',
                          height:312,
                          width:425,
                        ),
                          Column(
                            children: [
                              Image.asset(
                                'assets/icons/Reward4_icon.png',
                                height: 50,
                                width: 50,
                              ),
                         Stack(
                            alignment: Alignment.center,
                            children: [
                              ColorFiltered(
                                colorFilter: ColorFilter.mode(
                                  Colors.purple.withOpacity(0.7),
                                  BlendMode.srcATop,
                                ),
                                child: Image.asset(
                                  'assets/icons/Reward3_icon.png', // Replace with your background image path
                                  height:143 , // Adjust as needed
                                  width: 305, // Adjust as needed
                                ),
                              ),
                        const Text(
                          '1000 Points',
                          style: TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                            color: Colors.purple,
                          ),
                        ),
                      ],
                    ), 
                      ],
                    ),
                      ],
                    ),
                    
                    // Highlighted section ends here
                    const SizedBox(height: 20),
                    const Divider(color: Colors.white),
                    const ListTile(
                      title: Text('Bonus', style: TextStyle(color: Colors.white)),
                      trailing: Icon(Icons.arrow_forward, color: Colors.white),
                    ),
                    const Divider(color: Colors.white),
                    const ListTile(
                      title: Text('Get Daily Points', style: TextStyle(color: Colors.white)),
                      trailing: Icon(Icons.arrow_forward, color: Colors.white),
                    ),
                    const Divider(color: Colors.white),
                    const ListTile(
                      title: Text('Invite Friends to win rewards', style: TextStyle(color: Colors.white)),
                      trailing: Icon(Icons.arrow_forward, color: Colors.white),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
      )
    );
  }
}




   
  
