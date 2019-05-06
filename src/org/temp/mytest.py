/class Tank():
    #tank初始化
    def __init__(self,name,fire):
        self.name=name
        self.fire=fire
        print("fire="+self.fire)

    #tank运动方法：顺时针旋转５°
    def run_1(self,situation):
        print(situation + "=0")

    #tank运动方法：逆时针旋转５°
    def run_2(self,situation):
        print(situation + "=1")

    # tank运动方法：反向走
    def run_3(self,situation):
        print(situation + "=2")

    # tank运动方法：顺时针旋转90°
    def run_4(self,situation):
        print(situation + "=3")

    # tank运动方法：逆时针旋转５°
    def run_5(self,situation):
        print(situation + "=4")

# 我方tank撞到岩石
tank_crash_stone=True

# 我方tank撞到边界
tank_crash_border=True

# 我方tank撞到敌方tank
tank_crash_enemy=True




# 为你的tank起个名字，同时定义开火方式，0:向敌方开火;1:向中间开火;
mytank=Tank('mytank','0')

# 当我方tank撞到边界
if(tank_crash_border):
    mytank.run_2("tank_crash_border")

# 当我方tank撞到岩石
if(tank_crash_stone):
    mytank.run_3("tank_crash_stone")

# 当我方tank撞到敌方tank
if(tank_crash_enemy):
    mytank.run_4("tank_crash_enemy")
