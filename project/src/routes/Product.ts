import express from 'express';
import multer from 'multer';
import {
    getAllProducts,
    getOneProduct,
    createProduct,
    updateProduct,
    deleteProduct,
    purchasedProduct,
    buyProduct,
    getBuyerProduct,
    searchProduct,
    uploadImage,
    deleteImage,
} from '../controllers/Product';
import { auth } from '../middlewares/auth';

const upload = multer({
    storage: multer.diskStorage({
      destination: 'uploads/',
      filename: function (req, file, cb) {
        cb(null, Date.now() + '-' + file.originalname);
      },
    }),
  });

const ProductRouter = express.Router();

ProductRouter.get('/', getAllProducts);

ProductRouter.get('/one', auth, getOneProduct);

ProductRouter.post('/', auth, createProduct);

ProductRouter.patch('/', auth, updateProduct);

ProductRouter.delete('/', auth, deleteProduct);

ProductRouter.post('/purchased', auth, purchasedProduct);

ProductRouter.post('/buy', auth, buyProduct);

ProductRouter.get('/buyer', auth, getBuyerProduct);

ProductRouter.post('/search', searchProduct);

ProductRouter.post('/image/:id', auth, upload.single('image'), uploadImage);

ProductRouter.delete('/image/:id', auth, deleteImage);

export default ProductRouter;
