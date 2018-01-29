using SecurityStreet.Server.Models.Dto;
using SecurityStreet.Server.Services.Autovelox.Models;
using ServiceStack;
using ServiceStack.Data;
using ServiceStack.OrmLite;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Web.Script.Serialization;

namespace SecurityStreet.Server.Services.Autovelox
{
    public class NotificationService : Service
    {
        /// <summary>
        /// The database connection factory
        /// </summary>
        protected IDbConnectionFactory dbConnectionFactory;

        /// <summary>
        /// Gets or sets the automatic query.
        /// </summary>
        /// <value>
        /// The automatic query.
        /// </value>
        public IAutoQueryDb AutoQuery { get; set; }

        /// <summary>
        /// Initializes a new instance of the <see cref="BaseCrudService{Entity, Dto, ReadRequest, UpdateRequest, DeleteRequest}"/> class.
        /// </summary>
        public NotificationService()
        {
            dbConnectionFactory = HostContext.Resolve<IDbConnectionFactory>();
        }



        /// <summary>
        /// Anies the specified request.
        /// </summary>
        /// <param name="request">The request.</param>
        /// <returns></returns>
        /// <exception cref="ArgumentException">
        /// Request cannot be null
        /// or
        /// Request item cannot be null
        /// </exception>
        public NotificationSubscriptionDto Any(SubscriptionRequest request)
        {
            if (request == null)
                throw new ArgumentException("Request cannot be null");

            if (request.Item == null)
                throw new ArgumentException("Request item cannot be null");

            using (var db = dbConnectionFactory.Open())
            {
                try
                {
                    List<Server.Models.Entities.NotificationSubscription> existing = db.LoadSelect<Server.Models.Entities.NotificationSubscription>(n => n.ClientToken == request.Item.ClientToken);

                    if (existing != null && existing.Count > 0)
                    {
                        var record = existing.FirstOrDefault();
                        record.Radius = request.Item.Radius >= 0 ? request.Item.Radius : 10;
                        record.Longitude = request.Item.Longitude;
                        record.Latitude = request.Item.Latitude;

                        db.Save(record);

                        return db.LoadSingleById<Server.Models.Entities.NotificationSubscription>(record.Id).ConvertTo<NotificationSubscriptionDto>();
                    }

                    db.Save(request.Item.ConvertTo<Server.Models.Entities.NotificationSubscription>());

                    return db.LoadSingleById<Server.Models.Entities.NotificationSubscription>(request.Item.Id).ConvertTo<NotificationSubscriptionDto>();
                }
                catch (Exception e)
                {
                    return null;
                }
            }
        }


        /// <summary>
        /// Posts the specified request.
        /// </summary>
        /// <param name="request">The request.</param>
        /// <returns></returns>
        /// <exception cref="ArgumentException">
        /// Request cannot be null
        /// or
        /// ClientToken cannot be null
        /// </exception>
        public bool Post(UnsubscriptionRequest request)
        {
            if (request == null)
                throw new ArgumentException("Request cannot be null");

            if (request.ClientToken.IsNullOrEmpty())
                throw new ArgumentException("ClientToken cannot be null");

            using (var db = dbConnectionFactory.Open())
            {
                try
                {
                    int result = db.Delete<Server.Models.Entities.NotificationSubscription>(s => s.ClientToken == request.ClientToken);

                    return result > 0;
                }
                catch (Exception e)
                {
                    throw e;
                }
            }
        }
    }
}