from turtle import* #거북이
from random import* #무작위

def A(a): #반올림 #파이썬ㅡㅡ
    if a%1 >= 0.5:
        b = a+1-(a%1)
    elif a%1 < 0.5:
        b = a-(a%1)
    return b

def Map(): #맵 그리기
    shape("turtle")
    right(180)
    speed(0)
    penup()
    goto(-200,300)
    pendown()
    goto(200,300)
    goto(200,200)
    goto(-200,200)
    goto(-200,100)
    goto(200,100)
    goto(200,0)
    goto(-200,0)
    goto(-200,-100)
    goto(200,-100)
    goto(200,-200)
    goto(-200,-200)
    goto(-200,-300)
    goto(200,-300)
    penup()
    for i in range(7):
        for j in range(5):
            goto(100*j-200,100*i-285)
            if position()==(-200,315) or position()==(-200,15):
                color("green")
                write("여관")
            elif position()==(100,315) or position()==(-100,215) or position()==(100,115) or position()==(0,15) or position()==(-100,-85) or position()==(0,-185) or position()==(200,-185) or position()==(-100,-285):
                color("orange")
            elif position()==(0,215) or position()==(100,215) or position()==(0,115) or position()==(-100,115) or position()==(100,15):
                color("blue")
                write("빙판")
            elif position()==(0,-85) or position()==(200,-85) or position()==(100,-185) or position()==(-100,-185) or position()==(0,-285):
                color("purple")
                write("늪")
            elif position()==(200,-285):
                color("yellow")
                write("목표")
            else:
                color("white")
            pendown()
            begin_fill()
            circle(15)
            end_fill()
            penup()
    for i in range(7):
        for j in range(5):
            goto(100*j-200,100*i-285)
            if position()==(-200,315) or position()==(-200,15):
                color("green")
            elif position()==(100,315):
                color("orange")
                write("1.5")
            elif position()==(-100,215) or position()==(100,115):
                color("orange")
                write("2.5")
            elif position()==(0,15) or position()==(-100,-85):
                color("orange")
                write("3.5")
            elif position()==(0,-185) or position()==(200,-185):
                color("orange")
                write("4.5")
            elif position()==(-100,-285):
                color("orange")
                write("5.5")
            elif position()==(0,215) or position()==(100,215) or position()==(0,115) or position()==(-100,115) or position()==(100,15):
                color("blue")
            elif position()==(0,-85) or position()==(200,-85) or position()==(100,-185) or position()==(-100,-185) or position()==(0,-285):
                color("purple")
            elif position()==(200,-285):
                color("yellow")
            else:
                color("black")
            pendown()
            circle(15)
            penup()
    home()
    goto(-200,300)
            
def Clone(): #땅찍기
    if position() == (-200,300) or position() == (-200,0):
        color("green") #여관
    elif position()==(100,300) or position()==(-100,200) or position()==(100,100) or position()==(0,0) or position()==(-100,-100) or position()==(0,-200) or position()==(200,-200) or position()==(-100,-300):
        color("orange") #몬스터
    elif position()==(0,200) or position()==(100,200) or position()==(-100,100) or position()==(0,100) or position()==(100,0):
        color("blue") #빙판
    elif position()==(0,-100) or position()==(200,-100) or position()==(-100,-200) or position()==(100,-200) or position()==(0,-300):
        color("purple") #늪
    elif position()==(200,-300):
        color("yellow") #목표
    else:
        color("white") #땅
    clone()

def Game(): #인게임 #c:차례 #d:행동의사 #e:주사위
    global c
    c = 2 #초기 순서
    s1 = 0 #초기 늪값
    s2 = 0
    x1 = -200 #초기 1p 상태 
    y1 = 300
    z1 = 0
    x2 = -200 #초기 2p 상태
    y2 = 300
    z2 = 0
    rx1 = -200 #초기 1p 부활값
    ry1 = 300
    rz1 = 0
    rx2 = -200 #초기 2p 부활값
    ry2 = 300
    rz2 = 0
    i1 = 0 #초기 1p 아이템값
    i2 = 0 #초기 2p 아이템값
    t = [] #초기 덫값
    j = -1 #초기 덫 제거값
    while True:
        c = c + 1
        if c == 3:
            c = 1
        if c == 1:
            if s1 != 0:
                s1 = s1 - 1
                continue
        elif c == 2:
            if b == 1:
                continue
            if s2 != 0:
                s2 = s2 - 1
                continue
        if c == 1:
            goto(x1,y1)
            setheading(z1)
            if (x1,y1) != (x2,y2):
                Clone() #땅찍기
            color("black")
        elif c == 2:
            goto(x2,y2)
            setheading(z2)
            if (x1,y1) != (x2,y2):
                Clone() #땅찍기
            color("red")
        while True:
            if c == 1:
                d = int(input("검은색의 차례입니다.\n1.주사위 던지기\n2.아이템 사용하기\n>>>"))
            elif c == 2:
                print("빨간색의 차례입니다.")
                if b == 2:
                    d = int(input("1.주사위 던지기\n2.아이템 사용하기\n>>>"))
                else: #인공지능
                    if i2 == 1 and y1 < y2:
                        d = 2
                    elif i2 == 2 and y1 > y2:
                        d = 2
                    else:
                        d = 1
            if d == 1: #주사위 던지기
                e = randint(1,6)
                print("주사위 :",e)
                speed(1.2) #이동 및 위치저장
                for i in range(e):
                    forward(100)
                    x = A(xcor())
                    y = A(ycor())
                    goto(x,y)
                    if position() == (200,-300):
                        goto(200,-285)
                        write("승리!")
                        goto(200,-300)
                        break
                    if xcor() == -200:
                        left(90)
                        if ycor() == 0:
                            break
                    if xcor() == 200:
                        right(90)
                for i in range(len(t)):
                    if position() == t[i]: #덫
                        print("덫에 걸렸습니다!")
                        j=i
                        if c == 1:
                            s1 += 1
                        elif c == 2:
                            s2 += 1
                if position()==(0,200) or position()==(100,200) or position()==(-100,100) or position()==(0,100) or position()==(100,0): #빙판
                    print("빙판입니다!")
                    while True:
                        if b == 3 and c == 2:
                            d = 1
                        else:
                            d = int(input("주사위를 던져주세요.\n1.주사위 던지기\n>>>"))
                        if d == 1:
                            e = randint(1,6)
                            print("주사위 :",e)
                            if e >= 4:
                                print("슝슝~")
                                for i in range(3):
                                    forward(100)
                                    x = A(xcor())
                                    y = A(ycor())
                                    goto(x,y)
                                    if xcor() == -200:
                                        left(90)
                                    if xcor() == 200:
                                        right(90)
                                break
                            elif e<4:
                                print("미끄러졌습니다.")
                                for i in range(2):
                                    if xcor() == -200:
                                        right(90)
                                    if xcor() == 200:
                                        left(90)
                                    backward(100)
                                    x = A(xcor())
                                    y = A(ycor())
                                    goto(x,y)
                                break
                        else:
                            print("잘못 입력하셨습니다.")
                if position()==(0,-100) or position()==(200,-100) or position()==(-100,-200) or position()==(100,-200) or position()==(0,-300): #늪
                    print("늪입니다!")
                    while True:
                        if b == 3 and c == 2:
                            d = 1
                        else:
                            d = int(input("주사위를 던져주세요.\n1.주사위 던지기\n>>>"))
                        if d == 1:
                            e = randint(1,6)
                            print("주사위 :",e)
                            if e >= 4:
                                print("첨~벙! 첨~벙!")
                                break
                            elif e<4:
                                print("늪에 빠졌습니다.")
                                if c == 1:
                                    s1 = s1 + 1
                                elif c == 2:
                                    s2 = s2 + 1
                                break
                        else:
                            print("잘못 입력하셨습니다.")
                if position()==(100,300) or position()==(-100,200) or position()==(100,100) or position()==(0,0) or position()==(-100,-100) or position()==(0,-200) or position()==(200,-200) or position()==(-100,-300):
                    if position()==(100,300): #몬스터 #p:전투력 #d:행동의사 #e:주사위
                        print("슬라임을 만났습니다! (전투력 1.5)")
                        p = 1.5
                    elif position()==(-100,200) or position()==(100,100):
                        print("고블린을 만났습니다! (전투력 2.5)")
                        p = 2.5
                    elif position()==(0,0) or position()==(-100,-100):
                        print("오우거를 만났습니다! (전투력 3.5)")
                        p = 3.5
                    elif position()==(0,-200) or position()==(200,-200):
                        print("드래곤을 만났습니다! (전투력 4.5)")
                        p = 4.5
                    elif position()==(-100,-300):
                        print("마왕을 만났습니다! (전투력 5.5)")
                        p = 5.5
                    if j != -1:
                        print("덫에 걸린 상태로 몬스터를 만나 패배하셨습니다... (사망)")
                        print("여관에서 부활합니다.")
                        if c == 1:
                            goto(rx1,ry1)
                            setheading(rz1)
                        elif c == 2:
                            goto(rx2,ry2)
                            setheading(rz2)
                    else:
                        while True:
                            if b == 3 and c == 2:
                                d = 1
                            else:
                                d = int(input("주사위를 던져주세요!\n1.주사위 던지기\n>>>"))
                            if d == 1:
                                e = randint(1,6)
                                print("주사위 :",e)
                                if e > p:
                                    print("승리하셨습니다!")
                                    e = randint(0,1)
                                    if e == 1: #아이템 획득
                                        e = randint(1,2)
                                        if e == 1:
                                            print("아이템 '짱돌'을 획득하셨습니다.")
                                            if (c == 1 and i1 != 0) or (c == 2 and i2 != 0):
                                                if c == 2 and b == 3:
                                                    i2 = 1
                                                else:
                                                    while True:
                                                        d = int(input("이미 다른 아이템을 소지하고 있습니다.\n기존의 아이템을 버리고 '짱돌'을 획득하시겠습니까?\n1.예\n2.아니오\n>>>"))
                                                        if d == 1:
                                                            if c == 1:
                                                                i1 = 1
                                                            elif c == 2:
                                                                i2 = 1
                                                            break
                                                        elif d == 2:
                                                            break
                                                        else:
                                                            print("잘못 입력하셨습니다.")
                                            else:
                                                if c == 1:
                                                    i1 = 1
                                                elif c == 2:
                                                    i2 = 1
                                        elif e == 2:
                                            print("아이템 '덫'을 획득하셨습니다.")
                                            if (c == 1 and i1 != 0) or (c == 2 and i2 != 0):
                                                if c == 2 and b == 3:
                                                    i2 = 2
                                                else:
                                                    while True:
                                                        d = int(input("이미 다른 아이템을 소지하고 있습니다.\n기존의 아이템을 버리고 '덫'을 획득하시겠습니까?\n1.예\n2.아니오\n>>>"))
                                                        if d == 1:
                                                            if c == 1:
                                                                i1 = 2
                                                            elif c == 2:
                                                                i2 = 2
                                                            break
                                                        elif d == 2:
                                                            break
                                                        else:
                                                            print("잘못 입력하셨습니다.") #아이템 추가 예정?
                                            else:
                                                if c == 1:
                                                    i1 = 2
                                                elif c == 2:
                                                    i2 = 2
                                    break
                                elif e < p:
                                    print("패배하셨습니다... (사망)")
                                    print("여관에서 부활합니다.")
                                    if c == 1:
                                        goto(rx1,ry1)
                                        setheading(rz1)
                                        break
                                    elif c == 2:
                                        goto(rx2,ry2)
                                        setheading(rz2)
                                        break
                            else:
                                print("잘못 입력하셨습니다.")
                if j != -1: # 덫 제거
                    t.remove(t[j])
                    j = -1
                clone()
                speed(0)
                if c == 1: #위치저장
                    x1 = xcor()
                    y1 = ycor()
                    z1 = heading()
                elif c == 2:
                    x2 = xcor()
                    y2 = ycor()
                    z2 = heading()
                if c == 1 and position() == (-200,0): #부활값 갱신
                    rx1 = -200
                    ry1 = 0
                    rz1 = 270
                if c == 2 and position() == (-200,0):
                    rx2 = -200
                    ry2 = 0
                    rz2 = 270
                break
            elif d == 2: #아이템
                if c == 1: #1p 아이템 사용
                    if i1 == 0:
                        print("소지하고 있는 아이템이 없습니다")
                    elif i1 == 1:
                        while True:
                            d = int(input("아이템 '짱돌'을 사용하시겠습니까?\n1.예\n2.아니오\n>>>"))
                            if d == 1:
                                if y1 > y2:
                                    print("1p가 '짱돌'을 사용하였습니다.")
                                    i1 = 0
                                    while True:
                                        d = int(input("주사위를 던져주세요.\n1.주사위 던지기\n>>>"))
                                        if d == 1:
                                            e = randint(1,6)
                                            print("주사위 :",e)
                                            if e < 4:
                                                print("빗나갔습니다...")
                                            elif e > 3:
                                                clone()
                                                shape("circle")
                                                shapesize(0.7,0.7,1)
                                                speed(1)
                                                goto(x2,y2)
                                                print("퍽!")
                                                speed(0)
                                                goto(x1,y1)
                                                speed(1.2)
                                                shape("turtle")
                                                shapesize(1,1,1)
                                                Clone()
                                                color("black")
                                                s2 += 1
                                            break
                                        else:
                                            print("잘못 입력하셨습니다.")
                                    break
                                else:
                                    print("아이템을 사용할 수 없습니다.")
                                    break
                            elif d == 2:
                                print("아이템 사용을 취소합니다.")
                                break
                            else:
                                print("잘못 입력하셨습니다.")
                    elif i1 == 2:
                        while True:
                            d = int(input("아이템 '덫'을 사용하시겠습니까?\n1.예\n2.아니오\n>>>"))
                            if d == 1:
                                if y1 < y2:
                                    print("1p가 '덫'을 사용하였습니다.")
                                    i1 = 0
                                    setheading(90)
                                    backward(5)
                                    shape("arrow")
                                    shapesize(0.6,1,1)
                                    clone()
                                    forward(5)
                                    shapesize(1,1,1)
                                    shape("turtle")
                                    setheading(z1)
                                    t.append(position())
                                    break
                                else:
                                    print("아이템을 사용할 수 없습니다.")
                                    break
                            elif d == 2:
                                print("아이템 사용을 취소합니다.")
                                break
                            else:
                                print("잘못 입력하셨습니다.")
                elif c == 2:
                    if b == 3: #컴퓨터 인공지능 아이템 사용
                        if i2 == 1:
                            print("2p가 '짱돌'을 사용하였습니다.")
                            i2 = 0
                            e = randint(1,6)
                            print("주사위 :",e)
                            if e < 4:
                                print("빗나갔습니다.")
                            elif e > 3:
                                clone()
                                shape("circle")
                                shapesize(0.7,0.7,1)
                                speed(1)
                                goto(x1,y1)
                                print("퍽!")
                                speed(0)
                                goto(x2,y2)
                                speed(1.2)
                                shape("turtle")
                                shapesize(1,1,1)
                                Clone()
                                color("red")
                                s1 += 1
                        elif i2 == 2:
                            print("2p가 '덫'을 사용하였습니다.")
                            i2 = 0
                            setheading(90)
                            backward(5)
                            shape("arrow")
                            shapesize(0.6,1,1)
                            clone()
                            forward(5)
                            shapesize(1,1,1)
                            shape("turtle")
                            setheading(z2)
                            t.append(position())
                    elif b == 2: #2p 아이템 사용
                        if i2 == 0:
                            print("소지하고 있는 아이템이 없습니다")
                        elif i2 == 1:
                            while True:
                                d = int(input("아이템 '짱돌'을 사용하시겠습니까?\n1.예\n2.아니오\n>>>"))
                                if d == 1:
                                    if y1 < y2:
                                        print("2p가 '짱돌'을 사용하였습니다.")
                                        i2 = 0
                                        while True:
                                            d = int(input("주사위를 던져주세요.\n1.주사위 던지기\n>>>"))
                                            if d == 1:
                                                e = randint(1,6)
                                                print("주사위 :",e)
                                                if e < 4:
                                                    print("빗나갔습니다...")
                                                elif e > 3:
                                                    clone()
                                                    shape("circle")
                                                    shapesize(0.7,0.7,1)
                                                    speed(1)
                                                    goto(x1,y1)
                                                    print("퍽!")
                                                    speed(0)
                                                    goto(x2,y2)
                                                    speed(1.2)
                                                    shape("turtle")
                                                    shapesize(1,1,1)
                                                    Clone()
                                                    color("red")
                                                    s1 += 1
                                                break
                                            else:
                                                print("잘못 입력하셨습니다.")
                                        break
                                    else:
                                        print("아이템을 사용할 수 없습니다.")
                                        break
                                elif d == 2:
                                    print("아이템 사용을 취소합니다.")
                                    break
                                else:
                                    print("잘못 입력하셨습니다.")
                        elif i2 == 2:
                            while True:
                                d = int(input("아이템 '덫'을 사용하시겠습니까?\n1.예\n2.아니오\n>>>"))
                                if d == 1:
                                    if y1 > y2:
                                        print("2p가 '덫'을 사용하였습니다.")
                                        i2 = 0
                                        setheading(90)
                                        backward(5)
                                        shape("arrow")
                                        shapesize(0.6,1,1)
                                        clone()
                                        forward(5)
                                        shapesize(1,1,1)
                                        shape("turtle")
                                        setheading(z2)
                                        t.append(position())
                                        break
                                    else:
                                        print("아이템을 사용할 수 없습니다.")
                                        break
                                elif d == 2:
                                    print("아이템 사용을 취소합니다.")
                                    break
                                else:
                                    print("잘못 입력하셨습니다.")
            else:
                print("잘못 입력하셨습니다.")
        if position() == (200,-300):
            break

while True: #인트로 #a:게임의사 #b:모드
    a = int(input("☆거북용사★\n1.시작하기\n2.설명보기\n3.종료하기\n>>>"))
    if a == 1:
        showturtle()
        b = int(input("☆시작하기★\n1.혼자서\n2.둘이서\n3.컴퓨터와\n>>>"))
        if b == 1 or b == 2 or b == 3:
            Map()
            Game()
            break
        else:
            print('잘못 입력하셨습니다.')
    elif a == 2: #설명
        print("이 프로그램은 턴제 주사위 보드게임입니다.\n1p:검은색, 2p:빨간색\n녹색:여관-사망 시 부활장소\n여관에 도착하면 부활장소를 갱신하고 그 즉시 멈춥니다.\n파란색:빙판-주사위 던지기\n1/2/3:뒤로 2칸, 4/5/6:앞으로 3칸")
        print("보라색:늪-주사위 던지기\n1/2/3:1턴 강제휴식, 4/5/6:영향없음\n주황색:몬스터-주사위 던지기\n몬스터의 전투력보다 크면 승리, 작으면 패배(사망)\n노란색:목표-도착 시 승리")
        print("아이템은 몬스터 처치 시 일정 확률로 획득 가능합니다.\n아이템은 최대 1개까지만 소지할 수 있습니다.\n아이템은 이동 이전에만 사용할 수 있습니다.(이동 후 사용불가)\n아이템 사용은 턴이 소모되지 않습니다.(아이템 사용 후 이동가능)")
        print("아이템 목록\n1.짱돌:사용 시 상대방에게 짱돌을 던집니다.\n상대방이 자신보다 아래에 있을 때 사용가능\n1/2/3:영향없음, 4/5/6:기절(상대 1턴 강제휴식)\n2.덫:사용 시 자신이 있는 자리에 덫을 설치합니다.\n상대방이 자신보다 위에 있을 때 사용가능\n덫에 걸린 상태로 몬스터를 만나면 무조건 패배합니다.\n덫은 설치한 사람이 걸릴 수도 있습니다.(주의)")
    elif a == 3:
        print("종료합니다.")
        break
    else:
        print("잘못 입력하셨습니다.")

if a == 1: #종료
    if c == 1:
        print("검은색이 승리하였습니다!")
    elif c == 2:
        print("빨간색이 승리하였습니다!")
    print("게임이 끝났습니다. 화면을 클릭하시면 종료됩니다.")
    exitonclick()
