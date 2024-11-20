const express = require('express');
const router = express.Router();
const db = require('../models/index');
const adminController = require('../controllers/dashboardController');
const initWebRouters = (app) => {
  router.get('/', (req, res) => {
    return res.send('GetDone');
  });
  router.get('/adb', (req, res) => {
    return res.send('KhanhVinh');
  });
  router.get('/get-test', async (req, res) => {
    try {
      let tour_order = await db.TourDetail.findAll({});
      return res.status(200).json(tour_order);
    } catch (error) {
      return res.send(error);
    }
  });
  // router.get('/Dashboard', async (req, res) => {
  //   return res.render("admin/index.ejs", {})
  // })
  router.get('/Dashboard', adminController.getDashBoard);
  router.get('/voucher', (req, res) => {
    return res.render('admin/voucher.ejs');
  });
  router.get('/voucher/add', (req, res) => {
    return res.render('admin/voucherAdd.ejs');
  });
  router.get('/voucher/modify/:id', (req, res) => {
    const voucherId = req.params.id;
    return res.render('admin/voucherModify.ejs',{voucherId});
  });
  return app.use('/', router);
};

module.exports = initWebRouters;
