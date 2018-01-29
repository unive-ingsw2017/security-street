using SecurityStreet.Server.Models.Base;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SecurityStreet.Server.Models.Dto
{
    public class NotificationSubscriptionDto : BaseEntity
    {
        /// <summary>
        /// Gets or sets the latitude.
        /// </summary>
        /// <value>
        /// The latitude.
        /// </value>
        public double Latitude { get; set; }

        /// <summary>
        /// Gets or sets the longitude.
        /// </summary>
        /// <value>
        /// The longitude.
        /// </value>
        public double Longitude { get; set; }

        /// <summary>
        /// Gets or sets the client token.
        /// </summary>
        /// <value>
        /// The client token.
        /// </value>
        public string ClientToken { get; set; }

        /// <summary>
        /// Gets or sets the radius in KM.
        /// </summary>
        /// <value>
        /// The radius.
        /// </value>
        public int Radius { get; set; }
    }
}
