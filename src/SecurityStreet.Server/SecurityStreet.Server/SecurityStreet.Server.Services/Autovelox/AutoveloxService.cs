using SecurityStreet.Server.Models.Dto;
using ServiceStack;
using ServiceStack.OrmLite;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;

namespace SecurityStreet.Server.Services.Autovelox
{
    /// <summary>
    /// The autovelox serivce
    /// </summary>
    /// <seealso cref="SecurityStreet.Server.Services.BaseService{SecurityStreet.Server.Models.Entities.Autovelox, SecurityStreet.Server.Models.Dto.AutoveloxDto, SecurityStreet.Server.Services.Autovelox.ReadAutoveloxRequest}" />
    public class AutoveloxService : BaseService<Server.Models.Entities.Autovelox, AutoveloxDto, ReadAutoveloxRequest>
    {
        /// <summary>
        /// Gets all the autovelox inside a centered circle the given coordinates and distance
        /// </summary>
        /// <param name="request">The request.</param>
        /// <returns></returns>
        public List<AutoveloxDto> Any(ReadAutoveloxByDistanceRequest request)
        {

            if (request == null)
                throw new ArgumentException("Request cannot be null");

            if (request.Latitude == 0 || request.Longitude == 0)
                throw new ArgumentException("Coordinates cannot be null");

            if (request.Distance <= 0)
                request.Distance = 100;

            var firstCoord = new GeoCoordinate(request.Latitude, request.Longitude);

            using (var db = dbConnectionFactory.Open())
            {
                try
                {
                    var q = db.SelectLazy<Server.Models.Entities.Autovelox>()
                        .Where(a => new GeoCoordinate(a.Latitude, a.Longitude).GetDistanceTo(firstCoord) <= (request.Distance * 1000));

                    List<AutoveloxDto> results = new List<AutoveloxDto>();
                    q.Each(a => results.Add(a.ConvertTo<AutoveloxDto>()));

                    return results;
                }
                catch (Exception e)
                {
                    throw e;
                }
            }
        }
    }
}