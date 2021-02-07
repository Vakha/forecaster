import sys
from sys import argv

class SomeModel:

    @staticmethod
    def predict(features, forDate):
        # Deep Thought AI calculation
        return 42


if __name__ == "__main__":
    if len(argv) == 3:
        print("predictedAmount:" + str(SomeModel.predict(argv[1], argv[2])))
    else:
        sys.exit("Error! Not enough arguments")