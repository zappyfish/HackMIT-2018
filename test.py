from segment import Segmenter

s = Segmenter('dog.png')

s.threshold_and_morph(11)

s.auto_segment()