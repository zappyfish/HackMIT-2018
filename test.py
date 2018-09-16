from segment import Segmenter

s = Segmenter('test.jpeg')

s.threshold_and_morph(11)

s.auto_segment()