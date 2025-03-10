num = int(input()) # Вводим число в 10 сс.

def func(num) -> list: # Определяем функцию для получения массива с числами в 7с сс
    lst = []
    
    while num!=0: # С помощью цикла переводим число в обычную семеричную сс
        ost = num%7
        num//=7

        if ost>3: # Определяем условие, при котором необходимо положительное число в 7 сс заменить на отрицательное в 7с сс

            ost-=7 # Находим число, вычитая основание сс

            num+=1 #Увеличиваем число для выполнения целочисленного деление с округлением вверх на следующей итерации
        
        lst = [ost] + lst
        
    return lst

for i in func(num):
    print(f"{ {i} }" if i<0 else i, end = '') # Используем форматный вывод для отрицательных чисел
