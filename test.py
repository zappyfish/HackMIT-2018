from segment import Segmenter

s = Segmenter('test2.png')

s.threshold_and_morph(11)

s.auto_segment()