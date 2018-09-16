from segment import Segmenter

def test():
    s = Segmenter('test.png')

    s.threshold_and_morph(11)

    s.auto_segment()


def dog_test():
    pass