from livewiresegmentation import LiveWireSegmentation
import cv2
import numpy as np
import scipy.ndimage as nd

class Segmenter:

    WINDOW_NAME = "Segment"
    KERNEL = np.ones((5, 5), np.uint8)
    ERODE_KERNEL = np.ones((10, 10), np.uint8)

    def __init__(self, filename):
        self._img = cv2.imread(filename)
        # self._img = cv2.resize(self._img, (300, 300))
        im = cv2.GaussianBlur(self._img, (3, 3), 0)
        im = cv2.Laplacian(im, cv2.CV_8U)
        self._pre_thresh = im
        self.threshold_and_morph(5)
        print("filtered")
        cv2.imwrite('gray.png', self._grayscale)
        self._pressed_key = None
        self._last_xy = (0, 0)
        self._start_coords = self._last_xy
        self._path = []
        self._started = False

    def threshold_and_morph(self, thresh, kernel=KERNEL):
        thresh, im = cv2.threshold(self._pre_thresh, thresh, 255, cv2.THRESH_BINARY)
        im = cv2.cvtColor(im, cv2.COLOR_BGR2GRAY)
        # im = self.close(im, kernel)
        im = cv2.morphologyEx(im, cv2.MORPH_CLOSE, kernel)
        # im = self.erode(im, Segmenter.ERODE_KERNEL)
        # self._grayscale = self.erode(self._grayscale)
        thresh, im = cv2.threshold(im, 0, 255, cv2.THRESH_BINARY_INV)
        cv2.imshow('gray', im)
        cv2.waitKey(10)
        self._grayscale = im

    def nearest_black(self, x, y):
        search_radius = 1
        pts = [(x, y)]
        while True:
            print("searching")
            for pt in pts:
                if self._grayscale[pt[0]][pt[1]] == 0:
                    return pt
            pts = []
            search_radius += 1
            # TODO: handle out of bounds
            for i in range(x - search_radius, x + search_radius):
                for j in range(y - search_radius, y + search_radius):
                    pts.append((i, j))

    def close(self, im, kernel=KERNEL):
        return self.erode(self.dilate(im))

    def dilate(self, im, kernel=KERNEL):
        return cv2.dilate(im, kernel, iterations=2)

    def erode(self, im, kernel=ERODE_KERNEL):
        return cv2.erode(im, kernel, iterations=1)

    def add_next_path(self, start, end):
        for coord in self.compute_path(start, end):
            self._path.append(coord)

    def compute_path(self, start, end):
        algorithm = LiveWireSegmentation(self._grayscale)
        path = algorithm.compute_shortest_path(start, end)
        return path

    def onMouse(self, event, x, y, flags, param):
        if event == cv2.EVENT_LBUTTONDBLCLK:
            print("dlick!")
            pt = self.nearest_black(x, y)
            x = pt[0]
            y = pt[1]
            if not self._started:
                self._start_coords = (x, y)
                self._last_xy = (x, y)
                self._started = True
            else:
                next_coord = (x, y)
                self.add_next_path(self._last_xy, next_coord)
                self._last_xy = next_coord
                self.display_path(self._path)
        elif event == cv2.EVENT_LBUTTONDOWN:
            print("click")
            if self._started:
                pt = self.nearest_black(x, y)
                x = pt[0]
                y = pt[1]
                path = self.compute_path(self._last_xy, (x, y))
                test_path = [x for x in self._path]
                for x in path:
                    test_path.append(x)
                self.display_path(test_path)

    def segment(self):
        cv2.namedWindow(Segmenter.WINDOW_NAME)
        cv2.setMouseCallback(Segmenter.WINDOW_NAME, self.onMouse)
        cv2.imshow(Segmenter.WINDOW_NAME, self._img)
        while True:
            key = cv2.waitKey(50) & 0xFF

    def auto_segment(self, min_length=50):
        _th, contours, hierarchy = cv2.findContours(self._grayscale, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
        min_contours = []
        for contour in contours:
            if len(contour) > min_length:
                min_contours.append(contour)
        desired_contours = []
        for contour in min_contours:
            cur = [contour]
            img = self._img.copy()
            cv2.drawContours(img, cur, -1, [255, 0, 0], 3)
            cv2.imshow("Segmented", img)
            key = cv2.waitKey(0)
            if key == ord('y'):
                desired_contours.append(contour)
        img = self._img.copy()
        cv2.drawContours(img, desired_contours, -1, [255, 0, 0], 3)
        cv2.imshow("Segmented", img)
        cv2.waitKey(10) & 0xFF



    def display_path(self, path):
        img = self._img.copy()
        img = self._grayscale.copy()
        for i in range(1, len(path)):
            start = path[i - 1]
            end = path[i]
            cv2.line(img, start, end, (0, 255, 0), 2)
        cv2.imshow(Segmenter.WINDOW_NAME, img)


