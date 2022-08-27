import sys
import sqlite3 as sql
from repository import repo
from dtos import Vaccine, Supplier, Clinic, Logistic
from daos import _Vaccines

LOCATION = 0
SUPPLIER = 0
AMOUNT = 1
DATE = 2


def main():
    number_of_vaccines = 0
    repo.create_tables()
    with open(sys.argv[1], 'r', encoding="utf-8") as config:
        occurences = config.readline().split(",")
        for vaccine_line in range(int(occurences[0])):
            vaccine_args = config.readline().split(",")
            vaccine = Vaccine(*vaccine_args)
            repo.vaccines.insert(vaccine)
            number_of_vaccines += 1

        for supplier_line in range(int(occurences[1])):
            supplier_args = config.readline().split(",")
            supplier = Supplier(*supplier_args)
            repo.suppliers.insert(supplier)

        for clinic_line in range(int(occurences[2])):
            clinic_args = config.readline().split(",")
            clinic = Clinic(*clinic_args)
            repo.clinics.insert(clinic)

        for logistic_line in range(int(occurences[3])):
            logistic_args = config.readline().split(",")
            logistic = Logistic(*logistic_args)
            repo.logistics.insert(logistic)

    with open(sys.argv[3], 'w') as output:
        with open(sys.argv[2], 'r', encoding="utf-8") as orders:
            new_line = ""
            for order in orders:
                order_args = order.split(',')
                act(order_args, number_of_vaccines)
                if len(order_args) == 3:
                    number_of_vaccines += 1
                output_line = repo.output_line()
                output.write(new_line + f"{output_line[0]},{output_line[1]},{output_line[2]},{output_line[3]}")
                new_line = "\n"


def act(order_args, number_of_vaccines):
    if len(order_args) == 3:
        supplier_id = repo.suppliers.get_id(order_args[SUPPLIER])
        vaccine = Vaccine(number_of_vaccines + 1, order_args[DATE], supplier_id, order_args[AMOUNT])
        repo.vaccines.insert(vaccine)
        repo.suppliers.update_logistic(order_args[SUPPLIER], int(order_args[AMOUNT]))

    else:
        amount = int(order_args[AMOUNT])
        while(amount > 0):
            amount -= repo.vaccines.update_quantity(amount)
        repo.clinics.update_logistic(order_args[LOCATION], int(order_args[AMOUNT]))

if __name__ == '__main__':
    main()

