const db = require('../models');
const moment = require('moment');
const { Op, where } = require('sequelize');

let getTopUser = (option) => {
  return new Promise(async (resolve, reject) => {
    try {
      let myMoment = moment();
      let currentDay = myMoment.format('YYYY-MM-DD HH:mm:ss');
      let beforeThreeMonth = myMoment
        .subtract(option, 'months')
        .format('YYYY-MM-DD HH:mm:ss');
      let data = await db.UserTourOrder.findAll({
        attributes: [
          ['user_Id', 'userID'],
          [
            db.sequelize.fn('count', db.sequelize.col('tour_order_Id')),
            'countTrip',
          ],
        ],
        group: ['UserTourOrder.user_Id'],
        include: [
          {
            model: db.TourOrder,
            as: 'tourOrder',
            where: {
              order_date: {
                [Op.between]: [beforeThreeMonth, currentDay],
              },
            },
            attributes: [],
          },
          {
            model: db.User,
            as: 'user',
            where: {
              role: 0,
            },
            attributes: [
              ['name', 'name'],
              ['phone_number', 'phoneNumber'],
              ['email', 'email'],
            ],
          },
        ],
        raw: false, // true: -> cấu trúc JS, fasle -> cấu trúc Sequelize
        nest: true, //true -> lồng nhau, fasle -> không lồng
        limit: 10,
        order: db.sequelize.literal('countTrip DESC'),
      });
      return resolve({
        status: 0,
        data: data,
      });
    } catch (error) {
      console.log(error);
      reject(error);
    }
  });
};
let getInfoById = (inputId) => {
  return new Promise(async (resolve, reject) => {
    try {
      let data = await db.User.findOne({
        where: {
          id: inputId,
        },
        attributes: ['name', 'phone_number', 'email', 'address'],
      });
      if (!data) {
        return resolve({
          status: 1,
          message: 'Không tìm thấy người dùng',
        });
      }
      return resolve({
        status: 0,
        message: 'OK',
        data: data,
      });
    } catch (error) {
      reject(error);
    }
  });
};

module.exports = {
  getTopUser: getTopUser,
  getInfoById: getInfoById,
};
