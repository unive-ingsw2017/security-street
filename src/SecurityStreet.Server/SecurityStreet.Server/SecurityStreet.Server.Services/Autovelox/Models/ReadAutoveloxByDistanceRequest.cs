using System.Collections.Generic;
using ServiceStack;
using SecurityStreet.Server.Models.Dto;
using SecurityStreet.Server.Services.Models;

namespace SecurityStreet.Server.Services.Autovelox
{
    /// <summary>
    /// Gets all the autovelox inside a centered circle the given coordinates and distance
    /// </summary>
    /// <seealso cref="ServiceStack.IReturn{System.Collections.Generic.List{SecurityStreet.Server.Models.Dto.AutoveloxDto}}" />
    [Route("/autovelox/readByDistance/{Latitude}/{Longitude}/{Distance}", "GET")]
    [Route("/autovelox/readByDistance/{Latitude}/{Longitude}", "GET")]
    public class ReadAutoveloxByDistanceRequest : IReturn<List<AutoveloxDto>>
    {
        /// <summary>
        /// Gets or sets the latitude.
        /// </summary>
        /// <value>
        /// The latitude.
        /// </value>
        public float Latitude { get; set; }

        /// <summary>
        /// Gets or sets the longitude.
        /// </summary>
        /// <value>
        /// The longitude.
        /// </value>
        public float Longitude { get; set; }

        /// <summary>
        /// Gets or sets the distance in KM.
        /// </summary>
        /// <value>
        /// The distance.
        /// </value>
        public int Distance { get; set; }

    }
}